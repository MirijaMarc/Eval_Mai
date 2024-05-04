package com.evaluation.Eval.controller;

import java.time.LocalDate;
import java.time.LocalTime;
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
import com.evaluation.Eval.repository.Grand_prix_piloteRepository;
import com.evaluation.Eval.repository.PiloteRepository;
import com.evaluation.Eval.dto.Grand_prix_piloteDTO;
import com.evaluation.Eval.entity.Grand_prix_pilote;


@Controller
@RequestMapping("grand_prix_pilotes")
public class Grand_prix_piloteController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private Grand_prix_piloteRepository grand_prix_piloteRepository;

    @Autowired
    private Grand_prixRepository grand_prixRepository;

    @Autowired
    private PiloteRepository piloteRepository;




    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("pages/grand_prix_pilote");
        Pageable pageable = PageRequest.of(page, size);
        Page<Grand_prix_pilote> pages = grand_prix_piloteRepository.findAll(pageable);
        mv.addObject("grand_prix_pilotes", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        if (error.isPresent()) mv.addObject("error", error.get());
        mv.addObject("grand_prixs", grand_prixRepository.findAll());

        mv.addObject("pilotes", piloteRepository.findAll());


        return mv;
    }

    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/grand_prix_pilotes");
        try {
            String colonnes = "(idgrandprix,idpilote,temps_course,date_grand_prix)";
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
    public ModelAndView insert(@ModelAttribute Grand_prix_piloteDTO grand_prix_piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/grand_prix_pilotes");
        Grand_prix_pilote entity = new Grand_prix_pilote();
        entity.setGrandprix(grand_prixRepository.findById(grand_prix_piloteDTO.getGrandprix()).get());
        entity.setPilote(piloteRepository.findById(grand_prix_piloteDTO.getPilote()).get());
        entity.setTemps(LocalTime.parse(grand_prix_piloteDTO.getTemps()));
        entity.setDate(LocalDate.parse(grand_prix_piloteDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        grand_prix_piloteRepository.save(entity);
        return mv;
    }


    @PostMapping("update")
    public ModelAndView update(int id, Grand_prix_piloteDTO grand_prix_piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/grand_prix_pilotes");
        Grand_prix_pilote entity = new Grand_prix_pilote();
        entity.setId(id);
        entity.setGrandprix(grand_prixRepository.findById(grand_prix_piloteDTO.getGrandprix()).get());
        entity.setPilote(piloteRepository.findById(grand_prix_piloteDTO.getPilote()).get());
        entity.setTemps(LocalTime.parse(grand_prix_piloteDTO.getTemps()));
        entity.setDate(LocalDate.parse(grand_prix_piloteDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        grand_prix_piloteRepository.save(entity);
        return mv;
    }


    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/grand_prix_pilotes");
        grand_prix_piloteRepository.delete(id);
        return mv;
    }


}
