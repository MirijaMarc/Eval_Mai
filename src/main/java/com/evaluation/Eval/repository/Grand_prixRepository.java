package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Grand_prix;

@Repository
public interface Grand_prixRepository extends JpaRepository<Grand_prix, Integer> {
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM grand_prixs where etat_grand_prix>=0    
            """)
    public Page<Grand_prix> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM grand_prixs where etat_grand_prix>=0    
            """)
    public List<Grand_prix> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE grand_prixs set etat_grand_prix= -10 where id_grand_prix= :id RETURNING *
            """)
    public Grand_prix delete(int id);
}