package com.evaluation.Eval.controller.client;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb.PageDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.evaluation.Eval.dto.DevisDTO;
import com.evaluation.Eval.dto.Devis_travauxDTO;
import com.evaluation.Eval.dto.PageRequestDTO;
import com.evaluation.Eval.dto.PayementDevisDTO;
import com.evaluation.Eval.entity.Devis;
import com.evaluation.Eval.entity.Devis_travaux;
import com.evaluation.Eval.entity.Finition;
import com.evaluation.Eval.entity.Maison;
import com.evaluation.Eval.entity.Maisons_travaux;
import com.evaluation.Eval.entity.PayementDevis;
import com.evaluation.Eval.entity.Utilisateur;
import com.evaluation.Eval.entity.V_Devis;
import com.evaluation.Eval.entity.V_PrixMaison;
import com.evaluation.Eval.repository.DevisRepository;
import com.evaluation.Eval.repository.Devis_travauxRepository;
import com.evaluation.Eval.repository.FinitionRepository;
import com.evaluation.Eval.repository.MaisonRepository;
import com.evaluation.Eval.repository.PayementDevisRepository;
import com.evaluation.Eval.repository.UserRepository;
import com.evaluation.Eval.repository.V_DevisRepository;
import com.evaluation.Eval.security.Role;
import com.evaluation.Eval.util.Util;
import com.itextpdf.io.source.ByteArrayOutputStream;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import lombok.val;

@Controller
@RequestMapping("client")
public class ClientController {
    
    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @Autowired
    private FinitionRepository finitionRepository;


    @Autowired
    private MaisonRepository maisonRepository;

    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Devis_travauxRepository devis_travauxRepository;

    
    @Autowired
    private V_DevisRepository v_DevisRepository;


    @Autowired
    private PayementDevisRepository payementDevisRepository;


    @Role(value = "client")
    @PostMapping("export")
    public ResponseEntity<byte[]> exporter(@ModelAttribute Devis_travauxDTO devis_travauxDTO){
        Context context = new Context();
        List<Devis_travaux> maisons = devis_travauxRepository.findAllByDevis(devis_travauxDTO.getDevis());
        context.setVariable("devis_travaux", maisons);
        context.setVariable("devis", devisRepository.findById(devis_travauxDTO.getDevis()).get());
        context.setVariable("payements",payementDevisRepository.findByIdDevis(devis_travauxDTO.getDevis()));
        Query query = entityManager.createNativeQuery("SELECT COALESCE(sum(montant),0) somme from payement_devis where iddevis= :id");
        query.setParameter("id", devis_travauxDTO.getDevis());
        double somme = Double.parseDouble(query.getSingleResult().toString());
        context.setVariable("somme", somme);

        String html = templateEngine.process("pdf/export-Devis.html", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        try {
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "export-Devis.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @PostMapping("/validerMontant")
    @ResponseBody
    public String validerMontant(@RequestParam("montant") double montant, @RequestParam("id") int id) {
        Query query = entityManager.createNativeQuery("SELECT reste from v_devis where id= :id");
        query.setParameter("id", id);
        double reste = Double.parseDouble(query.getSingleResult().toString());
        if (montant > reste){
            return "Erreur, le montant "+ montant + " est supérieur au reste à payer "+ reste;
        }
        return "OK";
    }

    @Role(value = "client")
    @PostMapping("payer")
    public ModelAndView payer(@ModelAttribute PayementDevisDTO payementDevisDTO){
        ModelAndView mv = new ModelAndView("redirect:/client");
        PayementDevis payementDevis = new PayementDevis();
        try {
            System.out.println(payementDevisDTO.getDevis());
            payementDevis.setDevis(devisRepository.findById(payementDevisDTO.getDevis()).get()); 
            payementDevis.setMontant(entityManager,payementDevisDTO.getMontant());
            payementDevis.setDate(payementDevisDTO.getDate());
            payementDevis.setReference("");
            payementDevis = payementDevisRepository.save(payementDevis);
            payementDevis.setReference("REF/"+ payementDevis.getId());
            payementDevisRepository.save(payementDevis);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", e.getMessage());
            return mv;
        }
        return mv;
    }


    @Transactional
    @Role(value = "client")
    @PostMapping("insererDevis")
    public ModelAndView insererDevis(@ModelAttribute DevisDTO devisDTO, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("redirect:/client");
        Devis devis = new Devis();
        Maison maison = maisonRepository.findById(devisDTO.getMaison()).get();
        devis.setMaison(maison);
        Finition finition = finitionRepository.findById(devisDTO.getFinition()).get();
        devis.setFinition(finition);
        devis.setMontantTotal(maison.getMontantTotal(entityManager, finition));
        devis.setFinitionPourcentage(finition.getMarge());
        devis.setDureeTravaux(maison.getDuree());
        devis.setUtilisateur(userRepository.findById(devisDTO.getUtilisateur()).get());
        devis.setDate(LocalDateTime.now());
        devis.setReference("DEV/");
        devis.setLieu(devisDTO.getLieu());
        System.out.println(devisDTO.getDate() == "");
        if (devisDTO.getDate() != ""){
            System.out.println("miditr");
            devis.setDate(LocalDateTime.parse(devisDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }
        
        try {
            devis.setDateDebutTravaux(devisDTO.getDateDebutTravaux());
        } catch (Exception e) {
            mv.setViewName("redirect:/client/new-Devis");
            mv.addObject("error", e.getMessage());
            return mv;
        }
        devis = devisRepository.save(devis);
        devis.setReference("DEV/" + String.valueOf(devis.getId()));
        devisRepository.save(devis);


        List<Maisons_travaux> maisons_travauxs = maison.findAllTravaux(entityManager);
        for (Maisons_travaux maisons_travaux : maisons_travauxs) {
            Devis_travaux devis_travaux = new Devis_travaux();
            devis_travaux.setDevis(devis);
            devis_travaux.setTravaux(maisons_travaux.getTravaux());
            devis_travaux.setPrixUnitaire(maisons_travaux.getTravaux().getPrixUnitaire());
            devis_travaux.setQuantite(maisons_travaux.getQuantite());
            devis_travauxRepository.save(devis_travaux);
        }

        return mv;
    }

    @Role(value = "client")
    @GetMapping("new-Devis")
    public ModelAndView newDevis(HttpServletRequest request, Optional<String> error) {
        ModelAndView mv = new ModelAndView("pages/client/nouveauDevis");
        Utilisateur user = null;
        if (Util.isLoged(request)){
            user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
        if (error.isPresent()) mv.addObject("error", error.get());
        mv.addObject("iduser", user.getId());
        mv.addObject("isLoged", Util.isLoged(request));
        mv.addObject("finitions", finitionRepository.findAll());
        mv.addObject("maisons", V_PrixMaison.findAll(entityManager));
        return mv;
    }



    @Role(value = {"client"})
    @GetMapping
    public ModelAndView findAllByClient(PageRequestDTO pageRequestDTO,@RequestParam Optional<String> search,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/client/listeDevis");
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
        Page<V_Devis> pages = v_DevisRepository.findAllByClient(pageable, user.getId());
        if (search.isPresent()){
            pages = v_DevisRepository.findAllByClient(pageable, search.get().trim(), user.getId());
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


}
