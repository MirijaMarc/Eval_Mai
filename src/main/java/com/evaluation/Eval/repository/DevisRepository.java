package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Devis;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {


    @Query(nativeQuery = true, value = """
        SELECT * from deviss WHERE 
        CAST(numero_devis as varchar) ILIKE %:search% OR 
        CAST(idmaison as varchar) ILIKE %:search% OR 
        CAST(idfinition as varchar) ILIKE %:search% OR 
        CAST(iduser as varchar) ILIKE %:search% OR 
        CAST(montant as varchar) ILIKE %:search% OR 
        CAST(finition as varchar) ILIKE %:search% OR 
        CAST(duree as varchar) ILIKE %:search% OR 
        CAST(date_devis as varchar) ILIKE %:search% OR 
        CAST(date_debut_travaux as varchar) ILIKE %:search% and iduser=:client  
            """)
    public Page<Devis> findAllByClient(Pageable pageable,String search, int client);

    @Query(nativeQuery = true, value = """
        SELECT * FROM deviss where etat_devis>=0 and iduser=:client  
            """)
    public Page<Devis> findAllByClient(Pageable pageable,int client);

    
    @Query(nativeQuery = true, value = """
            SELECT * from deviss WHERE 
CAST(numero_devis as varchar) ILIKE %:search% OR 
CAST(idmaison as varchar) ILIKE %:search% OR 
CAST(idfinition as varchar) ILIKE %:search% OR 
CAST(iduser as varchar) ILIKE %:search% OR 
CAST(montant as varchar) ILIKE %:search% OR 
CAST(finition as varchar) ILIKE %:search% OR 
CAST(duree as varchar) ILIKE %:search% OR 
CAST(date_devis as varchar) ILIKE %:search% OR 
CAST(date_debut_travaux as varchar) ILIKE %:search%


            """)
    public Page<Devis> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM deviss where etat_devis>=0    
            """)
    public Page<Devis> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM deviss where etat_devis>=0    
            """)
    public List<Devis> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE deviss set etat_devis= -10 where id_devis= :id RETURNING *
            """)
    public Devis delete(int id);
}