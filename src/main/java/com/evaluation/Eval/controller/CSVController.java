package com.evaluation.Eval.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.evaluation.Eval.entity.Ecurie;
import com.evaluation.Eval.entity.Ecurie_pilote;
import com.evaluation.Eval.entity.Grand_prix;
import com.evaluation.Eval.entity.Grand_prix_pilote;
import com.evaluation.Eval.entity.Pilote;
import com.evaluation.Eval.repository.CsvRepository;
import com.evaluation.Eval.repository.EcurieRepository;
import com.evaluation.Eval.repository.Ecurie_piloteRepository;
import com.evaluation.Eval.repository.Grand_prixRepository;
import com.evaluation.Eval.repository.Grand_prix_piloteRepository;
import com.evaluation.Eval.repository.PiloteRepository;
import com.evaluation.Eval.util.Csv;
import com.evaluation.Eval.util.Erreur;
import com.evaluation.Eval.util.Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
@RequestMapping("csv")
public class CSVController {
    
    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private EcurieRepository ecurieRepository;


    @Autowired
    private PiloteRepository piloteRepository;


    @Autowired
    private Grand_prixRepository grand_prixRepository;


    @Autowired
    private Ecurie_piloteRepository ecurie_piloteRepository;


    @Autowired
    private Grand_prix_piloteRepository grand_prix_piloteRepository;

    @Autowired
    private CsvRepository csvRepository;



    @Transactional
    @PostMapping(value = "import-csv")
    public ModelAndView insertDonnees(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        ArrayList<Erreur> erreurs = Csv.importer(csvRepository, file.getOriginalFilename());
        if (Csv.hasError(erreurs)){
            mv.setViewName("pages/erreur");
            mv.addObject("erreurs", erreurs);
        }
        Ecurie.insertEcuriesCsv(entityManager);
        Pilote.insertPilotesCsv(entityManager);
        Ecurie_pilote.insetEcuriePiloteCsv(entityManager);
        Grand_prix.insertGPCsv(entityManager);
        Grand_prix_pilote.insertGrandPrixPilote(entityManager);
    return mv;
    }
}
