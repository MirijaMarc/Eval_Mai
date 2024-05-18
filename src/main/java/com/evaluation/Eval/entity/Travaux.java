package com.evaluation.Eval.entity;

import com.evaluation.Eval.util.Util;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Entity
@Table(name = "travauxs")
@Data
public class Travaux {
    @Id
    @Column(name = "id_travaux")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="idtype_travaux")
    private Type_travaux type_travaux;

    @Column(name="code_travaux")
    private String code;

    @Column(name="nom_travaux")
    private String nom;

    @ManyToOne
    @JoinColumn(name="unite_travaux")
    private Unite unite;

    @Column(name="prix_unitaire")
    private double prixUnitaire;

    @Column(name="etat_travaux")
    private int etat;





    public static Page<Travaux> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(Travaux.class, mot), Travaux.class);
        List<Travaux> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }


    public void setPrixUnitaire(double p)throws Exception{
        if (p<= 0) throw new Exception("Valeur "+ p + " inférieur ou égale à 0");
        this.prixUnitaire = p;
    }



}