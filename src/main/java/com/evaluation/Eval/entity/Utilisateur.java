package com.evaluation.Eval.entity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Utilisateur {


    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    private String numero;

    private String password;

    private int role;

    @Column(name = "etat_user")
    private int etat;



    public String getRole() {
        switch (role) {
            case 1:
                return "Client";
            case 10:
                return "BTP";
            default:
                return "Client";
        }
    }


    public void setRole(String r){
        
    }


    public void setRole(int i) {
        role = i;
    }


    public boolean isNumero(String numero){
        if (numero !=null && numero.trim().isEmpty()){
            String regex = "(0|[+]261)?(32|33|34|38)[0-9]{7}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher= pattern.matcher(numero);
            if (matcher.matches()){
                return true;
            }
        } 
        
        return false;
    }

}