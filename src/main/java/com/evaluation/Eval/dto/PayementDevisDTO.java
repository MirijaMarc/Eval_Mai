package com.evaluation.Eval.dto;

import lombok.Data;


@Data
public class PayementDevisDTO {
     private int id;
     private int devis;
     private double montant;
     private String date;
     private int etat;

}
