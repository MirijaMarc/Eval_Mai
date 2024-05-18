package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Maison;

@Repository
public interface MaisonRepository extends JpaRepository<Maison, Integer> {

    
    @Query(nativeQuery = true, value = """
            SELECT * from maisons WHERE 
CAST(nom_maison as varchar) ILIKE %:search% OR 
CAST(description_maison as varchar) ILIKE %:search% OR 

CAST(duree_construction as varchar) ILIKE %:search% OR 

            """)
    public Page<Maison> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM maisons where etat_maison>=0    
            """)
    public Page<Maison> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM maisons where etat_maison>=0    
            """)
    public List<Maison> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE maisons set etat_maison= -10 where id_maison= :id RETURNING *
            """)
    public Maison delete(int id);
}