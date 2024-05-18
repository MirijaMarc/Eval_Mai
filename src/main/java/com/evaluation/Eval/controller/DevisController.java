package com.evaluation.Eval.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
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


import com.evaluation.Eval.repository.DevisRepository;
import com.evaluation.Eval.repository.FinitionRepository;
import com.evaluation.Eval.repository.MaisonRepository;
import com.evaluation.Eval.repository.UserRepository;
import com.evaluation.Eval.dto.DevisDTO;
import com.evaluation.Eval.entity.Devis;
import com.evaluation.Eval.entity.Utilisateur;
import com.evaluation.Eval.util.ExportCsv;
import com.evaluation.Eval.util.ExportExcel;


@Controller
@RequestMapping("devis")
public class DevisController {


    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private DevisRepository devisRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private MaisonRepository maisonRepository;

    @Autowired
    private FinitionRepository finitionRepository;

    @Autowired
    private UserRepository userRepository;





    @GetMapping
    public ModelAndView findAll(PageRequestDTO pageRequestDTO,@RequestParam Optional<String> search,@RequestParam Optional<String> error, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("pages/devis");
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize());
        mv.addObject("sortBy", pageRequestDTO.getSortBy());
        if (!pageRequestDTO.getSortBy().equalsIgnoreCase("default")){
            Sort sort = Sort.by(pageRequestDTO.getDirection(), pageRequestDTO.getSortBy());
            pageable = PageRequest.of(pageRequestDTO.getPage(), pageRequestDTO.getSize(), sort);
        }
        Page<Devis> pages = devisRepository.findAll(pageable);
        if (search.isPresent()){
            pages = devisRepository.findAll(pageable, search.get().trim());
            if (search.get().split("\\s+").length >0){
                pages = Devis.findAllMultiMot(entityManager, pageable, search.get());
            }
            mv.addObject("search", search.get());
        }
        mv.addObject("direction", pageRequestDTO.getDirectionString());
        for (Devis devis : pages.getContent()) {
            System.out.println(devis);
        }
        mv.addObject("deviss", pages.getContent());
        mv.addObject("currentPage", pages.getNumber());
        mv.addObject("totalPages", pages.getTotalPages());
        mv.addObject("isLoged", Util.isLoged(request));
        if (Util.isLoged(request)){
            Utilisateur user = (Utilisateur)request.getSession().getAttribute("user");
            mv.addObject("role", user.getRole());
        }
        if (error.isPresent()) mv.addObject("error", error.get());
        mv.addObject("maisons", maisonRepository.findAll());

        mv.addObject("finitions", finitionRepository.findAll());

        mv.addObject("users", userRepository.findAll());


        return mv;
    }



    @PostMapping(value = "import-csv")
    public ModelAndView importer(@RequestParam("file") MultipartFile file){
        ModelAndView mv = new ModelAndView("redirect:/devis");
        try {
            String colonnes = "(numero_devis,idmaison,idfinition,iduser,montant,finition,duree,date_devis,date_debut_travaux)";
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
    public ModelAndView insert(@ModelAttribute DevisDTO devisDTO){
        ModelAndView mv = new ModelAndView("redirect:/devis");
        Devis entity = new Devis();
        entity.setMaison(maisonRepository.findById(devisDTO.getMaison()).get());
        entity.setFinition(finitionRepository.findById(devisDTO.getFinition()).get());
        entity.setUtilisateur(userRepository.findById(devisDTO.getUtilisateur()).get());
        entity.setMontantTotal(devisDTO.getMontantTotal());
        entity.setFinitionPourcentage(devisDTO.getFinitionPourcentage());
        entity.setDureeTravaux(devisDTO.getDureeTravaux());
        entity.setDate(LocalDateTime.parse(devisDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        try {
            entity.setDateDebutTravaux(devisDTO.getDateDebutTravaux());
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            return mv;
        }

        devisRepository.save(entity);
        return mv;
    }




    @PostMapping("update")
    public ModelAndView update(int id, DevisDTO devisDTO){
        ModelAndView mv = new ModelAndView("redirect:/devis");
        Devis entity = new Devis();
        entity.setId(id);
        entity.setMaison(maisonRepository.findById(devisDTO.getMaison()).get());
        entity.setFinition(finitionRepository.findById(devisDTO.getFinition()).get());
        entity.setUtilisateur(userRepository.findById(devisDTO.getUtilisateur()).get());
        entity.setMontantTotal(devisDTO.getMontantTotal());
        entity.setFinitionPourcentage(devisDTO.getFinitionPourcentage());
        entity.setDureeTravaux(devisDTO.getDureeTravaux());
        entity.setDate(LocalDateTime.parse(devisDTO.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
        try {
            entity.setDateDebutTravaux(devisDTO.getDateDebutTravaux());
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            return mv;
        }


        devisRepository.save(entity);
        return mv;
    }




    @PostMapping("delete")
    public ModelAndView delete(int id){
        ModelAndView mv = new ModelAndView("redirect:/devis");
        devisRepository.delete(id);
        return mv;
    }


    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf() throws Exception {
        Context context = new Context();
        List<Devis> deviss = devisRepository.findAll();
        context.setVariable("deviss", deviss);

        String html = templateEngine.process("pdf/export-devis.html", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        renderer.createPDF(outputStream);

        byte[] pdfBytes = outputStream.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "export-devis.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


    @GetMapping("export-csv")
    public void exportCSV(HttpServletResponse response){
        try {
            response.setContentType("text/csv");
            response.addHeader("Content-Disposition", "attachment; filename=export-devis.csv");
            List<Devis> deviss = devisRepository.findAll();
            new ExportCsv().writeToCsv(deviss, response.getWriter());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("export-excel")
    public void exportXL(HttpServletResponse response){
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=export-devis.xlsx";
            response.setHeader(headerKey, headerValue);
            List<Devis> deviss = devisRepository.findAll();
            ExportExcel<Devis> generator = new ExportExcel<>(deviss);
            generator.generateExcelFile(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
