package com.evaluation.Eval.entity;

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

    private String password;

    private int role;

    @Column(name = "etat_user")
    private int etat;



    public String getRole() {
        switch (role) {
            case 1:
                return "USER";
            case 10:
                return "ADMIN";
            default:
                return "USER";
        }
    }


    public void setRole(String r){
        
    }


    public void setRole(int i) {
        role = i;
    }

}