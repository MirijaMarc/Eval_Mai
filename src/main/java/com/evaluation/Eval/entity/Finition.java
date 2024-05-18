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
@Table(name = "finitions")
@Data
public class Finition {
    @Id
    @Column(name = "id_finition")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nom_finition")
    private String nom;

    @Column(name="marge")
    private double marge;

    @Column(name="etat_finition")
    private int etat;





    public static Page<Finition> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(Finition.class, mot), Finition.class);
        List<Finition> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }

    public void setMarge(double m)throws Exception{
        if (m <= 0) throw new Exception("Marge "+ m + "inférieur ou égale à 0");
        this.marge = m;
    }
}