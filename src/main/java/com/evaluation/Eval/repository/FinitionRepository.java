package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Finition;

@Repository
public interface FinitionRepository extends JpaRepository<Finition, Integer> {

    
    @Query(nativeQuery = true, value = """
            SELECT * from finitions WHERE 
CAST(nom_finition as varchar) ILIKE %:search% OR 
CAST(marge as varchar) ILIKE %:search%


            """)
    public Page<Finition> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM finitions where etat_finition>=0    
            """)
    public Page<Finition> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM finitions where etat_finition>=0    
            """)
    public List<Finition> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE finitions set etat_finition= -10 where id_finition= :id RETURNING *
            """)
    public Finition delete(int id);
}