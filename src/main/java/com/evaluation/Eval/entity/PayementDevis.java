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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import java.util.List;


@Entity
@Table(name = "payement_devis")
@Data
public class PayementDevis{
    @Id
    @Column(name = "id_payement_devis")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="iddevis")
    private Devis devis;


    private String reference;

    @Column(name="montant")
    private double montant;

    @Column(name="date_payement")
    private LocalDateTime date;

    @Column(name="etat_payement_devis")
    private int etat;



    public void setDate(String d)throws Exception{
        if (d == null || d.equals("")) throw new Exception("Veuillez inserer une date");
        LocalDateTime date = LocalDateTime.parse(d,DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
        if (date.isBefore(LocalDateTime.now())) throw new Exception("Date "+ date + " avant la date d'aujourd'hui");
        this.date = date;
    }



    public void setMontant(EntityManager entityManager, double m)throws Exception{
        if (m <= 0) throw new Exception("Montant "+ m + " invalide");
        Query query = entityManager.createNativeQuery("SELECT reste from v_devis where id= :id");
        query.setParameter("id", this.getDevis().getId());
        double reste = Double.parseDouble(query.getSingleResult().toString());
        if (m> reste) throw new Exception("Montant "+ m + " supérieur au reste à payer "+ reste); 
        this.montant = m;
    }



    public static Page<PayementDevis> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(PayementDevis.class, mot), PayementDevis.class);
        List<PayementDevis> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }
}