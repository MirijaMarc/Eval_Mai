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
@Table(name = "maisons_travaux")
@Data
public class Maisons_travaux {
    @Id
    @Column(name = "id_maison_travaux")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="idmaison")
    private Maison maison;

    @ManyToOne
    @JoinColumn(name="idtravaux")
    private Travaux travaux;

    @Column(name="quantite")
    private double quantite;





    public static Page<Maisons_travaux> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(Maisons_travaux.class, mot), Maisons_travaux.class);
        List<Maisons_travaux> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }
}