package com.evaluation.Eval.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.evaluation.Eval.util.Util;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
@Table(name = "v_devis")
public class V_Devis {
    @Id
    private int id;

    @ManyToOne
    @JoinColumn(name="idmaison")
    private Maison maison;


    @ManyToOne
    @JoinColumn(name="iduser")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name="idfinition")
    private Finition finition; 

    private double montant;
    private double reste;
    @Column(name = "date_debut_travaux")
    private LocalDate dateDebutTravaux;

    @Column(name = "statut")
    private int statut;


    @Column(name = "duree")
    private double duree;

    @Column(name = "pourcentage_paye")
    private double pourcentage;


    @Column(name = "payement_effectue")
    private double payementEffectue;


    private String reference;
    private String lieu;

    @Transient
    private LocalDate dateFinTravaux;


    public static List<V_Devis> findAllByUser(EntityManager entityManager, Utilisateur utilisateur){
        Query query = entityManager.createNativeQuery("SELECT * FROM v_devis where iduser = :id", V_Devis.class);
        return (List<V_Devis>) query.getResultList();
    }

        public static Page<V_Devis> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(V_Devis.class, mot), Devis.class);
        List<V_Devis> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);


    }

    
    public LocalDate getDateFinTravaux() {
        dateFinTravaux = dateDebutTravaux.plusDays((long)duree);
        return dateFinTravaux;
    }
}
