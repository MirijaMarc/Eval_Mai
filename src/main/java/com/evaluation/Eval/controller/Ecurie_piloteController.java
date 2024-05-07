package com.evaluation.Eval.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
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
import com.google.common.hash.Hashing;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.evaluation.Eval.repository.EcurieRepository;
import com.evaluation.Eval.repository.Ecurie_piloteRepository;
import com.evaluation.Eval.repository.PiloteRepository;
import com.evaluation.Eval.dto.Ecurie_piloteDTO;
import com.evaluation.Eval.entity.Ecurie_pilote;
import com.evaluation.Eval.util.ExportCsv;
import com.evaluation.Eval.util.ExportExcel;
import com.evaluation.Eval.security.NoSession;


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




    @NoSession
    @GetMapping
    public ModelAndView findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "8") int size,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/ecurie_pilote");
        Pageable pageable = PageRequest.of(page, size);
        Page<Ecurie_pilote> pages = ecurie_piloteRepository.findAll(pageable);
        mv.addObject("ecurie_pilotes", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("isLoged", Util.isLoged(request));
        if (error.isPresent()) mv.addObject("error", error.get());
        mv.addObject("ecuries", ecurieRepository.findAll());

        mv.addObject("pilotes", piloteRepository.findAll());


        return mv;
    }


    @NoSession
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


    @NoSession
    @PostMapping
    public ModelAndView insert(@ModelAttribute Ecurie_piloteDTO ecurie_piloteDTO){
        ModelAndView mv = new ModelAndView("redirect:/ecurie_pilotes");
        Ecurie_pilote entity = new Ecurie_pilote();
        entity.setEcurie(ecurieRepository.findById(ecurie_piloteDTO.getEcurie()).get());
        entity.setPilote(piloteRepository.findById(ecurie_piloteDTO.getPilote()).get());

        ecurie_piloteRepository.save(entity);
        return mv;
    }



    @NoSession
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



    @NoSession
    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/ecurie_pilotes");
        ecurie_piloteRepository.delete(id);
        return mv;
    }

    @NoSession
    @GetMapping("export-pdf")
    public void exportPDF(HttpServletResponse response){
        try {
            response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @NoSession
    @GetMapping("export-csv")
    public void exportCSV(HttpServletResponse response){
        try {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=documentCSV.csv");
            List<Ecurie_pilote> ecurie_pilotes = ecurie_piloteRepository.findAll();
            new ExportCsv().writeToCsv(ecurie_pilotes, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NoSession
    @GetMapping("export-excel")
    public void exportXL(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=documentExcel.xlsx";
            response.setHeader(headerKey, headerValue);
            List<Ecurie_pilote> ecurie_pilotes = ecurie_piloteRepository.findAll();
            ExportExcel<Ecurie_pilote> generator = new ExportExcel<>(ecurie_pilotes);
            generator.generateExcelFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String sha256hex2 = Hashing.sha256()
        .hashString("Zoky", StandardCharsets.UTF_8)
        .toString();

        System.out.println(sha256hex2);
    }




}
