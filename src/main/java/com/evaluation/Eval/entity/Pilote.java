package com.evaluation.Eval.entity;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Table(name = "pilotes")
@Data
public class Pilote {
    @Id
    @Column(name = "id_pilote")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nom_pilote")
    private String nom;

    @Column(name="date_naissance")
    private LocalDate date;

    @Column(name="etat_pilote")
    private int etat;

    public void setNom(String n)throws Exception{
        if (n == null){
            System.out.println("Null izy ato");
            throw new Exception("Nom pilote invalide"); 
        }else{
            if (n.trim() == "") throw new Exception("Nom pilote invalide");
            nom = n;
        }
    }


    public static void insertPilotesCsv(EntityManager entityManager){
        entityManager.createNativeQuery("""
            INSERT into pilotes (nom_pilote, date_naissance) 
            SELECT nom , CAST(datenaissance as DATE) FROM csv
            GROUP BY nom, datenaissance
                """).executeUpdate();
    }




}