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



}