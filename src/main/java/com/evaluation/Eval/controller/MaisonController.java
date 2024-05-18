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


import com.evaluation.Eval.repository.MaisonRepository;
import com.evaluation.Eval.dto.MaisonDTO;
import com.evaluation.Eval.entity.Maison;
import com.evaluation.Eval.util.ExportCsv;
import com.evaluation.Eval.util.ExportExcel;
import com.evaluation.Eval.security.NoSession;


@Controller
@RequestMapping("maisons")
public class MaisonController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private MaisonRepository maisonRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;





    @GetMapping
    public ModelAndView findAll(PageRequestDTO pageRequestDTO,@RequestParam Optional<String> search,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/maison");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        mv.addObject("sortBy", pageRequestDTO.getSortBy());
        if (!pageRequestDTO.getSortBy().equalsIgnoreCase("default")){
            Sort sort = Sort.by(pageRequestDTO.getDirection(), pageRequestDTO.getSortBy());
            pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), sort);
        }
        Page<Maison> pages = maisonRepository.findAll(pageable);
        if (search.isPresent()){
            pages = maisonRepository.findAll(pageable, search.get().trim());
            if (search.get().split("\\s+").length >0){
                pages = Maison.findAllMultiMot(entityManager, pageable, search.get());
            }
            mv.addObject("search", search.get());
        }
        mv.addObject("direction", pageRequestDTO.getDirectionString());
        mv.addObject("maisons", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("isLoged", Util.isLoged(request));
        if (error.isPresent()) mv.addObject("error", error.get());

        return mv;
    }



    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/maisons");
        try {
            String colonnes = "(nom_maison,description_maison,duree_construction)";
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
    public ModelAndView insert(@ModelAttribute MaisonDTO maisonDTO){
        ModelAndView mv = new ModelAndView("redirect:/maisons");
        Maison entity = new Maison();
        entity.setNom(maisonDTO.getNom());
        entity.setDescription(maisonDTO.getDescription());
        entity.setDuree(maisonDTO.getDuree());

        maisonRepository.save(entity);
        return mv;
    }




    @PostMapping("update")
    public ModelAndView update(int id, MaisonDTO maisonDTO){
        ModelAndView mv = new ModelAndView("redirect:/maisons");
        Maison entity = new Maison();
        entity.setId(id);
        entity.setNom(maisonDTO.getNom());
        entity.setDescription(maisonDTO.getDescription());
        entity.setDuree(maisonDTO.getDuree());

        maisonRepository.save(entity);
        return mv;
    }




    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/maisons");
        maisonRepository.delete(id);
        return mv;
    }


    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf() throws Exception {
        Context context = new Context();
        List<Maison> maisons = maisonRepository.findAll();
        context.setVariable("maisons", maisons);

        String html = templateEngine.process("pdf/export-maison.html", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        renderer.createPDF(outputStream);

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "export-maison.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


    @GetMapping("export-csv")
    public void exportCSV(HttpServletResponse response){
        try {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=export-maison.csv");
            List<Maison> maisons = maisonRepository.findAll();
            new ExportCsv().writeToCsv(maisons, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("export-excel")
    public void exportXL(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=export-maison.xlsx";
            response.setHeader(headerKey, headerValue);
            List<Maison> maisons = maisonRepository.findAll();
            ExportExcel<Maison> generator = new ExportExcel<>(maisons);
            generator.generateExcelFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
