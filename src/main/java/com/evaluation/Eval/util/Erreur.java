package com.evaluation.Eval.util;

import java.util.ArrayList;

import lombok.Data;


@Data
public class Erreur {
    int ligne;
    String erreur = "";



    public Erreur(int l){
        setLigne(l);
    }
}
