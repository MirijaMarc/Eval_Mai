package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Pilote;

@Repository
public interface PiloteRepository extends JpaRepository<Pilote, Integer> {
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM pilotes where etat_pilote>=0    
            """)
    public Page<Pilote> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM pilotes where etat_pilote>=0    
            """)
    public List<Pilote> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE pilotes set etat_pilote= -10 where id_pilote= :id RETURNING *
            """)
    public Pilote delete(int id);
}