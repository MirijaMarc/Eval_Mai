package com.evaluation.Eval.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.evaluation.Eval.repository.CsvRepository;
import com.evaluation.Eval.util.Csv;
import com.evaluation.Eval.util.Erreur;
import com.evaluation.Eval.util.Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Controller
@RequestMapping("csv")
public class CSVImportController {


    @PersistenceContext
    private EntityManager entityManager;


    @Autowired
    private CsvRepository csvRepository;


    @Transactional
    @PostMapping(value = "import-csv")
    public ModelAndView insertDonnees(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/film");
        ArrayList<Erreur> erreurs = Csv.importer(csvRepository, file.getOriginalFilename());
        if (Util.hasError(erreurs)){
            mv.setViewName("errors/erreur");
            mv.addObject("erreurs", erreurs);
            return mv;
        }

        // insert
        // insert
        // insert
        return mv;
    }






}
