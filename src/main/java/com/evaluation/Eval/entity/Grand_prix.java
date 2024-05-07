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
@Table(name = "grand_prixs")
@Data
public class Grand_prix {
    @Id
    @Column(name = "id_grand_prix")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nom_grand_prix")
    private String nom;

    @Column(name="etat_grand_prix")
    private int etat;



}