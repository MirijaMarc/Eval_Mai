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
@Table(name = "maisons")
@Data
public class Maison {
    @Id
    @Column(name = "id_maison")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nom_maison")
    private String nom;

    @Column(name="description_maison")
    private String description;

    @Column(name="etat_maison")
    private int etat;

    @Column(name="duree_construction")
    private double duree;




    public List<Maisons_travaux> findAllTravaux(EntityManager entityManager){
        Query query = entityManager.createNativeQuery("SELECT * FROM maisons_travaux where idmaison = :idmaison", Maisons_travaux.class);
        query.setParameter("idmaison", this.getId());
        return (List<Maisons_travaux>) query.getResultList();

    }

    public double getMontantTotal(EntityManager entityManager, Finition finition){
        String sql = "SELECT prix From v_prix_maisons where id=:idmaison LIMIT 1";
        Query query = entityManager.createNativeQuery(sql);
        System.out.println(this.getId());
        query.setParameter("idmaison", this.getId());
        System.out.println(query.getSingleResult().toString());
        double montant = Double.parseDouble(query.getSingleResult().toString());
        return montant + (montant*finition.getMarge()/100);
    }



    public static Page<Maison> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(Maison.class, mot), Maison.class);
        List<Maison> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }
}