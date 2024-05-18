package com.evaluation.Eval.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class V_Dashboard {
    
    @Id
    private int mois;


    @Column(name = "nombre_de_devis")
    private int nbrDevis;

    @Column(name = "total_montant")
    private double total;

    private String getMois(){
        switch (mois) {
            case 1:
                return "Janvier";
            case 2:
                return "Février";
            case 3:
                return "Mars";
            case 4:
                return "Avril";
            case 5:
                return "Mai";
            case 6:
                return "Juin";
            case 7:
                return "Juillet";
            case 8:
                return "Août";
            case 9:
                return "Septembre";
            case 10:
                return "Octobre";
            case 11:
                return "Novembre";
            case 12:
                return "Décembre";
            default:
                return "Décembre";
        }
    }

    public static List<V_Dashboard> getStatistique(EntityManager entityManager, int annee){
        String sql = """
            WITH mois_annee AS (
                SELECT *
                FROM generate_series(1,12) as mois
              ),
              devis_par_mois AS (
                SELECT EXTRACT(YEAR FROM date_devis) AS annee,
                       EXTRACT(MONTH FROM date_devis) AS mois,
                       COUNT(*) AS nombre_de_devis,
                       SUM(montant) AS total_montant
                FROM deviss
                GROUP BY EXTRACT(YEAR FROM date_devis), EXTRACT(MONTH FROM date_devis)
              )
              SELECT ma.mois,
                     COALESCE(dm.nombre_de_devis, 0) AS nombre_de_devis,
                     COALESCE(dm.total_montant, 0) AS total_montant
              FROM mois_annee ma
              LEFT JOIN devis_par_mois dm ON ma.mois = dm.mois AND dm.annee =:annee
                                           
              ORDER BY ma.mois
                """;
        Query query = entityManager.createNativeQuery(sql, V_Dashboard.class);
        query.setParameter("annee", annee);
        return (List<V_Dashboard>) query.getResultList();
    }
}
