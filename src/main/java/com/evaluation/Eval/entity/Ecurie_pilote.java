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



}