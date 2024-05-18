package com.evaluation.Eval.entity;

import com.evaluation.Eval.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Entity
@Table(name = "deviss")
@Data
public class Devis {
    @Id
    @Column(name = "id_devis")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="idmaison")
    private Maison maison;

    @ManyToOne
    @JoinColumn(name="idfinition")
    private Finition finition;

    @ManyToOne
    @JoinColumn(name="iduser")
    private Utilisateur utilisateur;

    @Column(name="montant")
    private double montantTotal;

    @Column(name="finition")
    private double finitionPourcentage;

    @Column(name="duree")
    private double dureeTravaux;

    @Column(name="date_devis")
    private LocalDateTime date;

    @Column(name="date_debut_travaux")
    private LocalDate dateDebutTravaux;

    @Column(name="etat_devis")
    private int etat;

    private String reference;

    private String lieu;


    @Transient
    private LocalDate dateFinTravaux;

    @Transient
    private double resteNonpaye;

    @Transient
    private double montantPaye;




    public void setDateDebutTravaux(String d)throws Exception{
        LocalDate date = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (date.isBefore(LocalDate.now())) throw new Exception("Date de debut de travaux inféreur à la date d'aujourd'hui");
        dateDebutTravaux = LocalDate.parse(d, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }


    
    public static double montantTotal(EntityManager entityManager){
        Query query = entityManager.createNativeQuery("SELECT COALESCE(sum(montant),0) FROM deviss");
        return Double.parseDouble(query.getSingleResult().toString());
    }

    public static double montantTotalEffectue(EntityManager entityManager){
        Query query = entityManager.createNativeQuery("select COALESCE(SUM(montant - reste),0) from v_devis");
        return Double.parseDouble(query.getSingleResult().toString());
    }




    public static Page<Devis> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(Devis.class, mot), Devis.class);
        List<Devis> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);


    }


    public double montantPaye(EntityManager entityManager){
        String sql ="""
            SELECT sum(montant) FROM payement_devis WHERE iddevis = :id
                """;
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("id", this.getId());
        double paye = Double.parseDouble(query.getSingleResult().toString());
        return paye;
        
    }





    public LocalDate getDateFinTravaux() {
        dateFinTravaux = dateDebutTravaux.plusDays((long)dureeTravaux);
        return dateFinTravaux;
    }
}