package com.evaluation.Eval.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;

import com.evaluation.Eval.repository.PiloteRepository;
import com.evaluation.Eval.dto.PiloteDTO;
import com.evaluation.Eval.entity.Pilote;
import com.evaluation.Eval.entity.Utilisateur;
import com.evaluation.Eval.util.ExportCsv;
import com.evaluation.Eval.util.ExportExcel;
import com.evaluation.Eval.security.NoSession;
import com.evaluation.Eval.security.Role;


@Controller
@RequestMapping("pilotes")
public class PiloteController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private PiloteRepository piloteRepository;





    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/pilote");
        Pageable pageable = PageRequest.of(page, size);
        Page<Pilote> pages = piloteRepository.findAll(pageable);
        mv.addObject("pilotes", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("isLoged", Util.isLoged(request));
        if (Util.isLoged(request)){
            Utilisateur user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
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



    @Role(value= "Admin")
    @PostMapping
    public ModelAndView insert(@ModelAttribute PiloteDTO piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        Pilote entity = new Pilote();
        entity.setNom(piloteDTO.getNom());
        try {
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
        entity.setNom(piloteDTO.getNom());
        entity.setDate(LocalDate.parse(piloteDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        piloteRepository.save(entity);
        return mv;
    }




    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/pilotes");
        piloteRepository.delete(id);
        return mv;
    }


    @GetMapping("export-pdf")
    public void exportPDF(HttpServletResponse response){
        try {
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    @GetMapping("export-csv")
    public void exportCSV(HttpServletResponse response){
        try {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=documentCSV.csv");
            List<Pilote> pilotes = piloteRepository.findAll();
            new ExportCsv().writeToCsv(pilotes, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("export-excel")
    public void exportXL(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=documentExcel.xlsx";
            response.setHeader(headerKey, headerValue);
            List<Pilote> pilotes = piloteRepository.findAll();
            ExportExcel<Pilote> generator = new ExportExcel<>(pilotes);
            generator.generateExcelFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
