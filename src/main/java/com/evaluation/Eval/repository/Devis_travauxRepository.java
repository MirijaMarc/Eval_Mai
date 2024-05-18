package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Devis_travaux;

@Repository
public interface Devis_travauxRepository extends JpaRepository<Devis_travaux, Integer> {

    

    @Query(nativeQuery = true, value = """
        SELECT * from devis_travaux where iddevis = :id
          """)
    public Page<Devis_travaux> findAllByDevis(Pageable pageable, int id);

    @Query(nativeQuery = true, value = """
          SELECT * from devis_travaux where iddevis = :id
            """)
    public List<Devis_travaux> findAllByDevis(int id);


    @Query(nativeQuery = true, value = """
            SELECT * from devis_travaux WHERE 
CAST(iddevis as varchar) ILIKE %:search% OR 
CAST(idtrauvaux as varchar) ILIKE %:search% OR 
CAST(prix_unitaire as varchar) ILIKE %:search%
CAST(quantite as varchar) ILIKE %:search% OR 

            """)
    public Page<Devis_travaux> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM devis_travaux where etat_devis_travaux>=0    
            """)
    public Page<Devis_travaux> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM devis_travaux where etat_devis_travaux>=0    
            """)
    public List<Devis_travaux> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE devis_travaux set etat_devis_travaux= -10 where id_devis_travaux= :id RETURNING *
            """)
    public Devis_travaux delete(int id);
}