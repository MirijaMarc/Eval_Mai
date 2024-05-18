package com.evaluation.Eval.dto;

import lombok.Data;


@Data
public class TravauxDTO {
     private int id;
     private int type_travaux;
     private String code;
     private String nom;
     private int unite;
     private double prixUnitaire;
     private int etat;

}
