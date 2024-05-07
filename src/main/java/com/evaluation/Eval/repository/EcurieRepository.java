package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Ecurie;

@Repository
public interface EcurieRepository extends JpaRepository<Ecurie, Integer> {
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM ecuries where etat_ecurie>=0    
            """)
    public Page<Ecurie> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM ecuries where etat_ecurie>=0    
            """)
    public List<Ecurie> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE ecuries set etat_ecurie= -10 where id_ecurie= :id RETURNING *
            """)
    public Ecurie delete(int id);
}