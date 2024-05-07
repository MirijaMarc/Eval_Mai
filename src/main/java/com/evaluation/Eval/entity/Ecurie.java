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



}