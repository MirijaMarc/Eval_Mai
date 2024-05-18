package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.PayementDevis;

@Repository
public interface PayementDevisRepository extends JpaRepository<PayementDevis, Integer> {



    @Query(nativeQuery = true, value = """
            SELECT * FROM payement_devis where iddevis = :id 
            """)
    public List<PayementDevis> findByIdDevis(int id);

    
    @Query(nativeQuery = true, value = """
            SELECT * from payement_devis WHERE 
            CAST(iddevis as varchar) ILIKE %:search% OR 
            CAST(montant as varchar) ILIKE %:search% OR 
            CAST(date_payement as varchar) ILIKE %:search%
            """)
    public Page<PayementDevis> findAll(Pageable pageable, String search);
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM payement_devis where etat_payement_devi>=0    
            """)
    public Page<PayementDevis> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM payement_devis where etat_payement_devi>=0    
            """)
    public List<PayementDevis> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE payement_devis set etat_payement_devi= -10 where id_payement_devi= :id RETURNING *
            """)
    public PayementDevis delete(int id);
}