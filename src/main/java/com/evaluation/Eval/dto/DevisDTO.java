package com.evaluation.Eval.dto;

import lombok.Data;


@Data
public class DevisDTO {
     private int id;
     private String numero;
     private int maison;
     private int finition;
     private int utilisateur;
     private double montantTotal;
     private double finitionPourcentage;
     private double dureeTravaux;
     private String lieu= "Tana";
     private String date;
     private String dateDebutTravaux;
     private int etat;

}
