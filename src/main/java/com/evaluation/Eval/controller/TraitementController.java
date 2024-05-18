package com.evaluation.Eval.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.evaluation.Eval.entity.Devis;
import com.evaluation.Eval.entity.Utilisateur;
import com.evaluation.Eval.security.Role;
import com.evaluation.Eval.util.Util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/")
public class TraitementController {

    @PersistenceContext
    private EntityManager entityManager;


    @Role(value= {"btp", "client"})
    @GetMapping("graphique")
    public ModelAndView graphique(HttpServletRequest request){
        ModelAndView mv = new ModelAndView("graphique");
        Utilisateur user = (Utilisateur) request.getSession().getAttribute("user");
        mv.addObject("isLoged", Util.isLoged(request));
        mv.addObject("role", user.getRole());
        return mv ;
    }
    

    @GetMapping
    public ModelAndView home(){
        return new ModelAndView("redirect:/user/login-user");
    }



    @GetMapping("/test")
    public ResponseEntity<Object> index(){
        Devis d = new Devis();
        d.setId(1);
        System.out.println(d.montantPaye(entityManager));
        // Ecurie.Test(ecurieRepository, piloteRepository, ecurie_piloteRepository);
        return ResponseEntity.ok("OK");
    }


    @GetMapping("/hello")
    public ModelAndView hello(){
        return new ModelAndView("hello");
    }



    @GetMapping("/export")
    public ModelAndView export(){
        return new ModelAndView("pdf/export");
    }


    @Transactional
    @PostMapping("/clean")
    public ModelAndView nettoyage(){
        entityManager.createNativeQuery("SELECT truncate_all()").getSingleResult();
        return new ModelAndView("redirect:/user/login-user");
    }


    
}
