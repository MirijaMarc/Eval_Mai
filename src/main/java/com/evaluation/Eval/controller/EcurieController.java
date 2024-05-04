package com.evaluation.Eval.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.evaluation.Eval.util.Util;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import com.evaluation.Eval.repository.EcurieRepository;
import com.evaluation.Eval.dto.EcurieDTO;
import com.evaluation.Eval.entity.Ecurie;


@Controller
@RequestMapping("ecuries")
public class EcurieController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private EcurieRepository ecurieRepository;




    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("pages/ecurie");
        Pageable pageable = PageRequest.of(page, size);
        Page<Ecurie> pages = ecurieRepository.findAll(pageable);
        mv.addObject("ecuries", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        if (error.isPresent()) mv.addObject("error", error.get());

        return mv;
    }

    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/ecuries");
        try {
            String colonnes = "(nom_ecurie)";
            String path = Util.CSV_PATH;
            String filePath= path + "/"+ file.getOriginalFilename();
            Util.importer(entityManager, colonnes, filePath);
        } catch (Exception e) {
            e.printStackTrace();
            mv.addObject("error", e.getMessage());
            return mv;
        }
        return mv;
    }



    @PostMapping
    public ModelAndView insert(@ModelAttribute EcurieDTO ecurieDTO){
        ModelAndView mv = new ModelAndView("redirect:/ecuries");
        Ecurie entity = new Ecurie();
        try {
            entity.setNom(ecurieDTO.getNom());
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            return mv;
        
        }

        ecurieRepository.save(entity);
        return mv;
    }


    @PostMapping("update")
    public ModelAndView update(int id, EcurieDTO ecurieDTO){
        ModelAndView mv = new ModelAndView("redirect:/ecuries");
        Ecurie entity = new Ecurie();
        entity.setId(id);
        try {
            entity.setNom(ecurieDTO.getNom());
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            return mv;
        
        }

        ecurieRepository.save(entity);
        return mv;
    }


    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/ecuries");
        ecurieRepository.delete(id);
        return mv;
    }


}
