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
@Table(name = "ecurie_pilotes")
@Data
public class Ecurie_pilote {
    @Id
    @Column(name = "id_ecurie_pilote")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="idecurie")
    private Ecurie ecurie;

    @ManyToOne
    @JoinColumn(name="idpilote")
    private Pilote pilote;

    @Column(name="etat_ecurie_pilote")
    private int etat;


    public static void insetEcuriePiloteCsv(EntityManager em){
        em.createNativeQuery("""
            INSERT INTO ecurie_pilotes (idpilote, idecurie)
            SELECT p.id_pilote, e.id_ecurie from csv c
            JOIN pilotes p ON p.nom_pilote = c.nom AND p.date_naissance = CAST(c.datenaissance as date)
            JOIN ecuries e ON e.nom_ecurie = c.ecurie
            GROUP BY p.id_pilote, e.id_ecurie
                """).executeUpdate();
    }

}