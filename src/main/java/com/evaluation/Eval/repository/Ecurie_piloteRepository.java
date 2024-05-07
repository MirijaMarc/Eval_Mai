package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Ecurie_pilote;

@Repository
public interface Ecurie_piloteRepository extends JpaRepository<Ecurie_pilote, Integer> {
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM ecurie_pilotes where etat_ecurie_pilote>=0    
            """)
    public Page<Ecurie_pilote> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM ecurie_pilotes where etat_ecurie_pilote>=0    
            """)
    public List<Ecurie_pilote> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE ecurie_pilotes set etat_ecurie_pilote= -10 where id_ecurie_pilote= :id RETURNING *
            """)
    public Ecurie_pilote delete(int id);
}