package com.evaluation.Eval.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.evaluation.Eval.entity.Devis;
import com.evaluation.Eval.entity.V_Devis;

@Repository
public interface V_DevisRepository extends JpaRepository<V_Devis, Integer>{

    @Query(nativeQuery = true, value = """
    SELECT * from v_devis WHERE 
    CAST(duree as varchar) ILIKE %:search% OR 
    CAST(montant as varchar) ILIKE %:search% OR 
    CAST(date_debut_travaux as varchar) ILIKE %:search% OR 
    CAST(reste as varchar) ILIKE %:search%
        """)
    public Page<V_Devis> findAll(Pageable pageable,String search);


    @Query(nativeQuery = true, value = """
    SELECT * from v_devis WHERE 
    CAST(duree as varchar) ILIKE %:search% OR 
    CAST(montant as varchar) ILIKE %:search% OR 
    CAST(date_debut_travaux as varchar) ILIKE %:search% OR 
    CAST(reste as varchar) ILIKE %:search% and iduser=:client  
        """)
    public Page<V_Devis> findAllByClient(Pageable pageable,String search, int client);

    @Query(nativeQuery = true, value = """
        SELECT * FROM v_devis where iduser=:client  
            """)
    public Page<V_Devis> findAllByClient(Pageable pageable,int client);
}
