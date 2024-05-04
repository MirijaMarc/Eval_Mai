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


import com.evaluation.Eval.repository.Grand_prixRepository;
import com.evaluation.Eval.dto.Grand_prixDTO;
import com.evaluation.Eval.entity.Grand_prix;


@Controller
@RequestMapping("grand_prixs")
public class Grand_prixController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private Grand_prixRepository grand_prixRepository;




    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("pages/grand_prix");
        Pageable pageable = PageRequest.of(page, size);
        Page<Grand_prix> pages = grand_prixRepository.findAll(pageable);
        mv.addObject("grand_prixs", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        if (error.isPresent()) mv.addObject("error", error.get());

        return mv;
    }

    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/grand_prixs");
        try {
            String colonnes = "(nom_grand_prix)";
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
    public ModelAndView insert(@ModelAttribute Grand_prixDTO grand_prixDTO){
        ModelAndView mv = new ModelAndView("redirect:/grand_prixs");
        Grand_prix entity = new Grand_prix();
        entity.setNom(grand_prixDTO.getNom());

        grand_prixRepository.save(entity);
        return mv;
    }


    @PostMapping("update")
    public ModelAndView update(int id, Grand_prixDTO grand_prixDTO){
        ModelAndView mv = new ModelAndView("redirect:/grand_prixs");
        Grand_prix entity = new Grand_prix();
        entity.setId(id);
        entity.setNom(grand_prixDTO.getNom());

        grand_prixRepository.save(entity);
        return mv;
    }


    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/grand_prixs");
        grand_prixRepository.delete(id);
        return mv;
    }


}
