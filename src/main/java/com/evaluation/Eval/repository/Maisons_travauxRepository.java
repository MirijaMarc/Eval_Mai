package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Maisons_travaux;

@Repository
public interface Maisons_travauxRepository extends JpaRepository<Maisons_travaux, Integer> {

    
    @Query(nativeQuery = true, value = """
            SELECT * from maisons_travaux WHERE 
CAST(idmaison as varchar) ILIKE %:search% OR 
CAST(idtrauvaux as varchar) ILIKE %:search%
CAST(quantite as varchar) ILIKE %:search% OR 

            """)
    public Page<Maisons_travaux> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM maisons_travaux where etat_maisons_travaux>=0    
            """)
    public Page<Maisons_travaux> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM maisons_travaux where etat_maisons_travaux>=0    
            """)
    public List<Maisons_travaux> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE maisons_travaux set etat_maisons_travaux= -10 where id_maisons_travaux= :id RETURNING *
            """)
    public Maisons_travaux delete(int id);
}