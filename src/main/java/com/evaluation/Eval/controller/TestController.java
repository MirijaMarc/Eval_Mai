package com.evaluation.Eval.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.evaluation.Eval.security.NoSession;
import com.evaluation.Eval.security.Role;

@Controller
@RequestMapping("/")
public class TestController {



    @GetMapping("graphique")
    public ModelAndView graphique(){
        ModelAndView mv = new ModelAndView("graphique");
        return mv ;
    }
    

    @GetMapping
    public ModelAndView home(){
        return new ModelAndView("home");
    }


    @GetMapping("/home")
    public ModelAndView index(){
        return home();
    }


    @GetMapping("/hello")
    public ModelAndView hello(){
        return new ModelAndView("hello");
    }

    
}
