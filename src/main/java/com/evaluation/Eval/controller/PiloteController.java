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


import com.evaluation.Eval.repository.PiloteRepository;
import com.evaluation.Eval.dto.PiloteDTO;
import com.evaluation.Eval.entity.Pilote;


@Controller
@RequestMapping("pilotes")
public class PiloteController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private PiloteRepository piloteRepository;




    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("pages/pilote");
        Pageable pageable = PageRequest.of(page, size);
        Page<Pilote> pages = piloteRepository.findAll(pageable);
        mv.addObject("pilotes", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        if (error.isPresent()) mv.addObject("error", error.get());

        return mv;
    }

    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        try {
            String colonnes = "(nom_pilote,date_naissance)";
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
    public ModelAndView insert(@ModelAttribute PiloteDTO piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        Pilote entity = new Pilote();
        try {
            entity.setNom(piloteDTO.getNom());
            entity.setDate(LocalDate.parse(piloteDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            return mv;
        
        }
       

        piloteRepository.save(entity);
        return mv;
    }


    @PostMapping("update")
    public ModelAndView update(int id, PiloteDTO piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        Pilote entity = new Pilote();
        entity.setId(id);
        try {
            entity.setNom(piloteDTO.getNom());
            entity.setDate(LocalDate.parse(piloteDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            return mv;
        }

        piloteRepository.save(entity);
        return mv;
    }


    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        piloteRepository.delete(id);
        return mv;
    }


}
