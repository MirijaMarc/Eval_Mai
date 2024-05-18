package com.evaluation.Eval.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import com.evaluation.Eval.util.Constante;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.itextpdf.io.source.ByteArrayOutputStream;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import com.evaluation.Eval.dto.PageRequestDTO;


import com.evaluation.Eval.repository.UniteRepository;
import com.evaluation.Eval.dto.UniteDTO;
import com.evaluation.Eval.entity.Unite;
import com.evaluation.Eval.util.ExportCsv;
import com.evaluation.Eval.util.ExportExcel;
import com.evaluation.Eval.security.NoSession;


@Controller
@RequestMapping("unites")
public class UniteController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private UniteRepository uniteRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;





    @GetMapping
    public ModelAndView findAll(PageRequestDTO pageRequestDTO,@RequestParam Optional<String> search,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/unite");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        mv.addObject("sortBy", pageRequestDTO.getSortBy());
        if (!pageRequestDTO.getSortBy().equalsIgnoreCase("default")){
            Sort sort = Sort.by(pageRequestDTO.getDirection(), pageRequestDTO.getSortBy());
            pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), sort);
        }
        Page<Unite> pages = uniteRepository.findAll(pageable);
        if (search.isPresent()){
            pages = uniteRepository.findAll(pageable, search.get().trim());
            if (search.get().split("\\s+").length >0){
                pages = Unite.findAllMultiMot(entityManager, pageable, search.get());
            }
            mv.addObject("search", search.get());
        }
        mv.addObject("direction", pageRequestDTO.getDirectionString());
        mv.addObject("unites", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("isLoged", Util.isLoged(request));
        if (error.isPresent()) mv.addObject("error", error.get());

        return mv;
    }



    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/unites");
        try {
            String colonnes = "(nom_unite)";
            String path = Constante.CSV_PATH;
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
    public ModelAndView insert(@ModelAttribute UniteDTO uniteDTO){
        ModelAndView mv = new ModelAndView("redirect:/unites");
        Unite entity = new Unite();
        entity.setNom(uniteDTO.getNom());

        uniteRepository.save(entity);
        return mv;
    }




    @PostMapping("update")
    public ModelAndView update(int id, UniteDTO uniteDTO){
        ModelAndView mv = new ModelAndView("redirect:/unites");
        Unite entity = new Unite();
        entity.setId(id);
        entity.setNom(uniteDTO.getNom());

        uniteRepository.save(entity);
        return mv;
    }




    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/unites");
        uniteRepository.delete(id);
        return mv;
    }


    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf() throws Exception {
        Context context = new Context();
        List<Unite> unites = uniteRepository.findAll();
        context.setVariable("unites", unites);

        String html = templateEngine.process("pdf/export-unite.html", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        renderer.createPDF(outputStream);

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "export-unite.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


    @GetMapping("export-csv")
    public void exportCSV(HttpServletResponse response){
        try {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=export-unite.csv");
            List<Unite> unites = uniteRepository.findAll();
            new ExportCsv().writeToCsv(unites, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("export-excel")
    public void exportXL(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=export-unite.xlsx";
            response.setHeader(headerKey, headerValue);
            List<Unite> unites = uniteRepository.findAll();
            ExportExcel<Unite> generator = new ExportExcel<>(unites);
            generator.generateExcelFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
