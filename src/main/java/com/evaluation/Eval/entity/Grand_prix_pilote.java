package com.evaluation.Eval.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "grand_prix_pilotes")
@Data
public class Grand_prix_pilote {
    @Id
    @Column(name = "id_grand_prix_pilote")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="idgrandprix")
    private Grand_prix grandprix;

    @ManyToOne
    @JoinColumn(name="idpilote")
    private Pilote pilote;

    @Column(name="temps_course")
    private LocalTime temps;

    @Column(name="date_grand_prix")
    private LocalDate date;

    @Column(name="etat_grand_prix_pilote")
    private int etat;

    public static void insertGrandPrixPilote(EntityManager em){
        em.createNativeQuery("""
            INSERT INTO grand_prix_pilotes (idgrandprix,idpilote, temps_course,date_grand_prix)
            SELECT gp.id_grand_prix, p.id_pilote, CAST(temps as TIME ), CAST(dategrandprix as DATE) FROM csv c 
            JOIN pilotes p ON p.nom_pilote = c.nom AND p.date_naissance = CAST(c.datenaissance as date)
            JOIN grand_prixs gp ON gp.nom_grand_prix = c.grandprix
                """).executeUpdate();

    }



}