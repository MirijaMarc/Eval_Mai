package com.evaluation.Eval.controller;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.evaluation.Eval.dto.UserDTO;
import com.evaluation.Eval.entity.Utilisateur;
import com.evaluation.Eval.repository.UserRepository;
import com.evaluation.Eval.security.NoSession;
import com.google.common.hash.Hashing;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("user")
public class UtilisateurController {
    

    @Autowired
    private UserRepository userRepository;

    @PostMapping("login-admin")
    public ModelAndView loginAdmin(@ModelAttribute UserDTO userDTO, HttpServletRequest request){
        ModelAndView mv = new ModelAndView("redirect:/admin/dashboard");
        Optional<Utilisateur> user = userRepository.findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if (user.isPresent()){
            Utilisateur utilisateur = user.get();
            utilisateur.setPassword("");
            HttpSession session = request.getSession();
            session.setAttribute("user", utilisateur);
            return mv;
        }else{
            mv.addObject("error", "Email ou mot de passe invalide");
            mv.setViewName("redirect:/user/login-admin");
            return mv;
        }
    }


    @PostMapping
    public ModelAndView login(@ModelAttribute UserDTO userDTO, HttpServletRequest request){
        System.out.println("miditra");
        ModelAndView mv = new ModelAndView("redirect:/client");
        Optional<Utilisateur> user = userRepository.findByNumero(userDTO.getNumero());
        if (user.isPresent()){
            Utilisateur utilisateur = user.get();
            utilisateur.setPassword("");
            HttpSession session = request.getSession();
            session.setAttribute("user", utilisateur);
        }else{
            Utilisateur nouveauUser = new Utilisateur();
            nouveauUser.setNumero(userDTO.getNumero());
            nouveauUser.setRole(1);
            userRepository.save(nouveauUser);
            nouveauUser = userRepository.findByNumero(userDTO.getNumero()).get();
            HttpSession session = request.getSession();
            session.setAttribute("user", nouveauUser);
        }
        return mv;
    }

    
    @PostMapping("/inscription")
    public ModelAndView inscription(@ModelAttribute UserDTO userDTO){
        ModelAndView mv = new ModelAndView("redirect:/user/login");
        if (!userDTO.getPassword().equals(userDTO.getRe_password())){
            mv.addObject("error", "Mot de passe diffiérent");
            mv.setViewName("redirect:/user/inscription");
        }
        String passwordHash = Hashing.sha256()
        .hashString(userDTO.getPassword(), StandardCharsets.UTF_8)
        .toString();
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(userDTO.getEmail());
            utilisateur.setEtat(0);
            utilisateur.setRole(1);
            utilisateur.setPassword(passwordHash);
            userRepository.save(utilisateur);
        } catch (Exception e) {
            mv.addObject("error", e.getMessage());
            mv.setViewName("redirect:/user/inscription");
            return mv;
        }

        return mv;
    }

    @NoSession
    @GetMapping("/logout")
    public ModelAndView logout(@ModelAttribute UserDTO userDTO, HttpServletRequest request){
        request.getSession().removeAttribute("user");
        return new ModelAndView("redirect:/user/login-user");
    }


    @NoSession
    @GetMapping("/erreurAuth")
    public ModelAndView erreurAuth(){
        ModelAndView mv= new ModelAndView("errors/erreurAuth");
        return mv;
    }

    


    @GetMapping("/login-user")
    public ModelAndView login(@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("login-user");
        if (error.isPresent()){
            mv.addObject("error", error.get());
        }
        return mv;
    }

    
    @GetMapping("/login-admin")
    public ModelAndView loginAdmin(@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("login-admin");
        if (error.isPresent()){
            mv.addObject("error", error.get());
        }
        return mv;
    }

    
    @GetMapping("/inscription")
    public ModelAndView sign_in(@RequestParam Optional<String> error){
        ModelAndView mv = new ModelAndView("inscription");
        if (error.isPresent()){
            mv.addObject("error", error.get());
        }
        return mv;
    }
}
