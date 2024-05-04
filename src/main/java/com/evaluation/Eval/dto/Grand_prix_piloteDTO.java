package com.evaluation.Eval.dto;

import lombok.Data;


@Data
public class Grand_prix_piloteDTO {
     private int id;
     private int grandprix;
     private int pilote;
     private String temps;
     private String date;
     private int etat;

}
