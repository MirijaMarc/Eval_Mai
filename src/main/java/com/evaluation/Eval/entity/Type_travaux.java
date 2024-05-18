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
@Table(name = "type_travaux")
@Data
public class Type_travaux {
    @Id
    @Column(name = "id_type_travaux")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="code_type_travaux")
    private String code;

    @Column(name="nom_type_travaux")
    private String nom;

    @Column(name="etat_type_travaux")
    private int etat;


  


    public static Page<Type_travaux> findAllMultiMot(EntityManager entityManager, Pageable pageable, String mot) {
        Query query = entityManager.createNativeQuery(Util.createRequeteMultiMot(Type_travaux.class, mot), Type_travaux.class);
        List<Type_travaux> resultList = query.getResultList();
        long count = resultList.size();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, count);
    }
}