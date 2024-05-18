package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Travaux;

@Repository
public interface TravauxRepository extends JpaRepository<Travaux, Integer> {

    
    @Query(nativeQuery = true, value = """
            SELECT * from travauxs WHERE 
CAST(idtype_travaux as varchar) ILIKE %:search% OR 
CAST(code_travaux as varchar) ILIKE %:search% OR 
CAST(nom_travaux as varchar) ILIKE %:search% OR 
CAST(unite_travaux as varchar) ILIKE %:search% OR 
CAST(prix_unitaire as varchar) ILIKE %:search%


            """)
    public Page<Travaux> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM travauxs where etat_travaux>=0    
            """)
    public Page<Travaux> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM travauxs where etat_travaux>=0    
            """)
    public List<Travaux> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE travauxs set etat_travaux= -10 where id_travaux= :id RETURNING *
            """)
    public Travaux delete(int id);
}