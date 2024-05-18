package com.evaluation.Eval.controller.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.evaluation.Eval.dto.Devis_travauxDTO;
import com.evaluation.Eval.dto.PageRequestDTO;
import com.evaluation.Eval.entity.Devis;
import com.evaluation.Eval.entity.DevisCSV;
import com.evaluation.Eval.entity.Devis_travaux;
import com.evaluation.Eval.entity.MaisonsTravauxCSV;
import com.evaluation.Eval.entity.PaiementCSV;
import com.evaluation.Eval.entity.Utilisateur;
import com.evaluation.Eval.entity.V_Dashboard;
import com.evaluation.Eval.entity.V_Devis;
import com.evaluation.Eval.entity.V_PrixMaison;
import com.evaluation.Eval.repository.DevisCSVRepository;
import com.evaluation.Eval.repository.DevisRepository;
import com.evaluation.Eval.repository.Devis_travauxRepository;
import com.evaluation.Eval.repository.MaisonTravauxCSVRepository;
import com.evaluation.Eval.repository.PaiementCSVRepository;
import com.evaluation.Eval.repository.V_DevisRepository;
import com.evaluation.Eval.security.Role;
import com.evaluation.Eval.util.Csv;
import com.evaluation.Eval.util.Erreur;
import com.evaluation.Eval.util.Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;


@Controller
@RequestMapping("admin")
public class AdminController {
    

    @Autowired
    private V_DevisRepository v_DevisRepository;

    @Autowired
    private Devis_travauxRepository devis_travauxRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DevisRepository devisRepository;


    @Autowired
    private PaiementCSVRepository paiementCSVRepository;


    @Autowired
    private DevisCSVRepository devisCSVRepository;


    @Autowired
    private MaisonTravauxCSVRepository maisonTravauxCSVRepository;


    @Transactional
    @Role(value="btp")
    @PostMapping(value = "import-paiement")
    @SuppressWarnings(value = "unchecked")
    public ModelAndView insertPaiement(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/admin/dashboard");
        HashMap<String, Object> map1 = PaiementCSV.verifyCSV(file);
        if (Util.hasError((ArrayList<Erreur>)map1.get("erreurs"))){
            mv.setViewName("errors/erreur");
            mv.addObject("erreurs", (ArrayList<Erreur>)map1.get("erreurs"));
            return mv;
        }
        paiementCSVRepository.saveAll((ArrayList<PaiementCSV>) map1.get("data"));
        entityManager.createNativeQuery("""
            INSERT INTO payement_devis (reference ,iddevis,montant, date_payement)
            SELECT 
                pc.ref_paiement,
                d.id_devis,
                CAST(pc.montant as double precision),
                to_timestamp(date_paiement, 'DD/MM/YYYY')
            FROM paiement_csv pc 
            JOIN deviss d ON d.reference = pc.ref_devis
            LEFT JOIN payement_devis pd ON pd.reference = pc.ref_paiement
            WHERE pd.id_payement_devis is null
                """).executeUpdate();

        entityManager.createNativeQuery("""
            TRUNCATE TABLE paiement_csv cascade
                """).executeUpdate();
        return mv;
    }



    @Transactional
    @Role(value="btp")
    @PostMapping(value = "import-donnee")
    @SuppressWarnings(value = "unchecked")
    public ModelAndView insertDonnees(@RequestParam("file1") MultipartFile file, @RequestParam("file2") MultipartFile file2){
        ModelAndView mv = new ModelAndView("redirect:/admin/dashboard");
        HashMap<String, Object> map1 = MaisonsTravauxCSV.verifyCSV(file);
        HashMap<String, Object> map2 = DevisCSV.verifyCSV(file2);
        boolean hasError = false;
        ArrayList<Erreur> errors = new ArrayList<Erreur>();
        if (Util.hasError((ArrayList<Erreur>)map1.get("erreurs"))){
            hasError = true;
            errors.addAll((ArrayList<Erreur>)map1.get("erreurs"));
        } 
        if (Util.hasError((ArrayList<Erreur>)map2.get("erreurs"))){
            hasError = true;
            errors.addAll((ArrayList<Erreur>)map2.get("erreurs"));
        }
        if (hasError){
            mv.setViewName("errors/erreur");
            mv.addObject("erreurs", errors);
            return mv;
        }
        maisonTravauxCSVRepository.saveAll((ArrayList<MaisonsTravauxCSV>) map1.get("data"));
        devisCSVRepository.saveAll((ArrayList<DevisCSV>)map2.get("data"));

        entityManager.createNativeQuery("""
            INSERT INTO maisons (nom_maison, description_maison, surface, duree_construction) 
            SELECT  
                distinct mt.type_maison, mt.description,
                CAST (mt.surface as double PRECISION) ,
                CAST (mt.duree_travaux as double PRECISION) 
            FROM maisons_travaux_csv mt
            LEFT JOIN maisons m ON m.nom_maison = mt.type_maison
            WHERE m.id_maison is null
                """).executeUpdate();

        entityManager.createNativeQuery("""
            INSERT INTO unites (nom_unite)
            SELECT
                distinct mt.unite 
                from maisons_travaux_csv mt
            LEFT JOIN unites u ON u.nom_unite = mt.unite
            WHERE u.id_unite is null
                """).executeUpdate();
        entityManager.createNativeQuery("""
            INSERT INTO travauxs (code_travaux, nom_travaux, unite_travaux, prix_unitaire)
            SELECT 
                DISTINCT code_travaux,
                type_travaux,
                u.id_unite,
                CAST (mt.prix_unitaire as double PRECISION)
            FROM maisons_travaux_csv mt
            JOIN unites u ON u.nom_unite = mt.unite
                """).executeUpdate();
        entityManager.createNativeQuery("""
            INSERT into maisons_travaux (idmaison, idtravaux, quantite)
            SELECT
                m.id_maison,
                t.id_travaux,
                CAST(quantite as double PRECISION)
            FROM maisons_travaux_csv mt
            JOIN maisons m ON m.nom_maison = mt.type_maison
            JOIN travauxs t ON t.nom_travaux = mt.type_travaux
            ON CONFLICT (idmaison, idtravaux) DO NOTHING
                """).executeUpdate();
        entityManager.createNativeQuery("""
            INSERT INTO users (numero)
            SELECT 
                distinct client
            FROM devis_csv dc
            LEFT JOIN users u ON u.numero = dc.client
            WHERE u.id_user is null
                """).executeUpdate();
        entityManager.createNativeQuery("""
            INSERT INTO finitions (nom_finition, marge)
            SELECT 
                DISTINCT finition,
                CAST(taux_finition as double PRECISION) 
            FROM devis_csv 
            LEFT JOIN finitions f ON f.nom_finition= finition
            WHERE f.id_finition is null
                """).executeUpdate();
        entityManager.createNativeQuery("""
            INSERT INTO deviss (
                reference, idmaison,idfinition,
                iduser, montant,finition , 
                duree, date_devis, date_debut_travaux,
                lieu )
            SELECT 
                ref_devis,
                m.id_maison,
                f.id_finition,
                u.id_user,
                vp.prix + (vp.prix*f.marge/100),
                f.marge,
                vp.duree,
                to_timestamp(dc.date_devis, 'DD/MM/YYYY'),
                to_date(dc.date_debut, 'DD/MM/YYYY'),
                dc.lieu
            FROM devis_csv dc 
            JOIN maisons m ON m.nom_maison = dc.type_maison
            JOIN finitions f ON f.nom_finition = dc.finition
            JOIN users u ON u.numero= dc.client
            JOIN v_prix_maisons vp on vp.id= m.id_maison
            LEFT JOIN deviss d ON d.reference = dc.ref_devis
            WHERE d.id_devis is null 
                """).executeUpdate();
        entityManager.createNativeQuery("""
            INSERT INTO devis_travaux (iddevis, idtravaux, prix_unitaire, quantite)
            SELECT
                d.id_devis,
                t.id_travaux,
                t.prix_unitaire,
                mt.quantite
            FROM devis_csv dc
            JOIN deviss d ON d.reference = dc.ref_devis
            JOIN maisons_travaux mt ON mt.idmaison = d.idmaison
            JOIN travauxs t ON t.id_travaux = mt.idtravaux
            ON CONFLICT(iddevis, idtravaux) DO NOTHING  
                """).executeUpdate();

        entityManager.createNativeQuery("""
            TRUNCATE TABLE maisons_travaux_csv cascade
                """).executeUpdate();

        entityManager.createNativeQuery("""
            TRUNCATE TABLE devis_csv cascade
                """).executeUpdate();
        return mv;
    }



    @Role(value = "btp")
    @GetMapping("importation")
    public ModelAndView importation(HttpServletRequest request, Optional<String> error){
        ModelAndView mv = new ModelAndView("pages/admin/import");
        Utilisateur user = null;
        if (Util.isLoged(request)){
            user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
        if (error.isPresent()) mv.addObject("error", error.get());
        mv.addObject("iduser", user.getId());
        mv.addObject("isLoged", Util.isLoged(request));
        return mv;

    }


    @Role(value = {"btp"})
    @GetMapping("devis")
    public ModelAndView allDevis(PageRequestDTO pageRequestDTO,@RequestParam Optional<String> search,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/admin/listeDevis");
        Utilisateur user = null;
        if (Util.isLoged(request)){
            user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        mv.addObject("sortBy", pageRequestDTO.getSortBy());
        if (!pageRequestDTO.getSortBy().equalsIgnoreCase("default")){
            Sort sort = Sort.by(pageRequestDTO.getDirection(), pageRequestDTO.getSortBy());
            pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), sort);
        }
        Page<V_Devis> pages = v_DevisRepository.findAll(pageable);
        if (search.isPresent()){
            pages = v_DevisRepository.findAll(pageable, search.get().trim());
            if (search.get().split("\\s+").length >0){
                pages = V_Devis.findAllMultiMot(entityManager, pageable, search.get());
            }
            mv.addObject("search", search.get());
        }
        mv.addObject("direction", pageRequestDTO.getDirectionString());
        mv.addObject("devis", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("isLoged", Util.isLoged(request));
        if (error.isPresent()) mv.addObject("error", error.get());


        return mv;
    }

    @Role(value = "btp")
    @GetMapping("dashboard")
    public ModelAndView dashboard(HttpServletRequest request, Optional<String> annee){
        ModelAndView mv = new ModelAndView("pages/admin/dashboard");
                Utilisateur user = null;
        if (Util.isLoged(request)){
            user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
        if (annee.isPresent()){
            mv.addObject("v_dashboard", V_Dashboard.getStatistique(entityManager, Integer.parseInt(annee.get())));
            mv.addObject("annee", annee.get());
        }else{
            mv.addObject("v_dashboard", V_Dashboard.getStatistique(entityManager, 2024));
            mv.addObject("annee", "2024");
        }
        mv.addObject("montantTotal", Devis.montantTotal(entityManager));
        mv.addObject("montantTotalEffectue", Devis.montantTotalEffectue(entityManager));
        mv.addObject("isLoged", Util.isLoged(request));
        return mv;
    }

    @Role(value = "btp")
    @GetMapping("details")
    public ModelAndView detailsDevis(PageRequestDTO pageRequestDTO, HttpServletRequest request, @RequestParam int iddevis){
        ModelAndView mv = new ModelAndView("pages/admin/detailDevis");
                Utilisateur user = null;
        if (Util.isLoged(request)){
            user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());

        Page<Devis_travaux> pages = devis_travauxRepository.findAllByDevis(pageable,iddevis);
        mv.addObject("isLoged", Util.isLoged(request));
        mv.addObject("travaux", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("devis", devisRepository.findById(iddevis).get());
        mv.addObject("iddevis", iddevis);
        return mv;
    }
}
