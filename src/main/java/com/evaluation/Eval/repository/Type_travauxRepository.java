package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Type_travaux;

@Repository
public interface Type_travauxRepository extends JpaRepository<Type_travaux, Integer> {

    
    @Query(nativeQuery = true, value = """
            SELECT * from type_travaux WHERE 
CAST(code_type_travaux as varchar) ILIKE %:search% OR 
CAST(nom_type_travaux as varchar) ILIKE %:search%


            """)
    public Page<Type_travaux> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM type_travaux where etat_type_travaux>=0    
            """)
    public Page<Type_travaux> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM type_travaux where etat_type_travaux>=0    
            """)
    public List<Type_travaux> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE type_travaux set etat_type_travaux= -10 where id_type_travaux= :id RETURNING *
            """)
    public Type_travaux delete(int id);
}