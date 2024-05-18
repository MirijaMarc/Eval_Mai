package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Unite;

@Repository
public interface UniteRepository extends JpaRepository<Unite, Integer> {

    
    @Query(nativeQuery = true, value = """
            SELECT * from unites WHERE 
CAST(nom_unite as varchar) ILIKE %:search%


            """)
    public Page<Unite> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM unites where etat_unite>=0    
            """)
    public Page<Unite> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM unites where etat_unite>=0    
            """)
    public List<Unite> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE unites set etat_unite= -10 where id_unite= :id RETURNING *
            """)
    public Unite delete(int id);
}