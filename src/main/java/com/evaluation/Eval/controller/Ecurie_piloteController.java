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
import com.evaluation.Eval.repository.Ecurie_piloteRepository;
import com.evaluation.Eval.repository.PiloteRepository;
import com.evaluation.Eval.dto.Ecurie_piloteDTO;
import com.evaluation.Eval.entity.Ecurie_pilote;


@Controller
@RequestMapping("ecurie_pilotes")
public class Ecurie_piloteController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private Ecurie_piloteRepository ecurie_piloteRepository;

    @Autowired
    private EcurieRepository ecurieRepository;

    @Autowired
    private PiloteRepository piloteRepository;




    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("pages/ecurie_pilote");
        Pageable pageable = PageRequest.of(page, size);
        Page<Ecurie_pilote> pages = ecurie_piloteRepository.findAll(pageable);
        mv.addObject("ecurie_pilotes", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        if (error.isPresent()) mv.addObject("error", error.get());
        mv.addObject("ecuries", ecurieRepository.findAll());

        mv.addObject("pilotes", piloteRepository.findAll());


        return mv;
    }

    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/ecurie_pilotes");
        try {
            String colonnes = "(idecurie,idpilote)";
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
    public ModelAndView insert(@ModelAttribute Ecurie_piloteDTO ecurie_piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/ecurie_pilotes");
        Ecurie_pilote entity = new Ecurie_pilote();
        entity.setEcurie(ecurieRepository.findById(ecurie_piloteDTO.getEcurie()).get());
        entity.setPilote(piloteRepository.findById(ecurie_piloteDTO.getPilote()).get());

        ecurie_piloteRepository.save(entity);
        return mv;
    }


    @PostMapping("update")
    public ModelAndView update(int id, Ecurie_piloteDTO ecurie_piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/ecurie_pilotes");
        Ecurie_pilote entity = new Ecurie_pilote();
        entity.setId(id);
        entity.setEcurie(ecurieRepository.findById(ecurie_piloteDTO.getEcurie()).get());
        entity.setPilote(piloteRepository.findById(ecurie_piloteDTO.getPilote()).get());

        ecurie_piloteRepository.save(entity);
        return mv;
    }


    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/ecurie_pilotes");
        ecurie_piloteRepository.delete(id);
        return mv;
    }


}
