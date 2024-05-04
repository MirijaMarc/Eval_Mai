package com.evaluation.Eval.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "ecuries")
@Data
public class Ecurie {
    @Id
    @Column(name = "id_ecurie")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nom_ecurie")
    private String nom;

    @Column(name="etat_ecurie")
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

    public static void insertEcuriesCsv(EntityManager em){
        em.createNativeQuery("""
            INSERT into ecuries (nom_ecurie)  
            SELECT ecurie FROM csv
            GROUP BY ecurie
                """).executeUpdate();

    }

}