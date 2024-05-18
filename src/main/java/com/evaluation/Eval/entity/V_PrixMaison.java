package com.evaluation.Eval.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "v_prix_maisons")
@Data
public class V_PrixMaison {
    @Id
    private int id;


    private String nom;

    private String description;

    private double duree;


    private double prix;


    public static List<V_PrixMaison> findAll(EntityManager entityManager){
        return(List<V_PrixMaison>) entityManager.createNativeQuery("SELECT * From v_prix_maisons", V_PrixMaison.class).getResultList();
    }
}
