package com.evaluation.Eval.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.evaluation.Eval.entity.Grand_prix_pilote;

@Repository
public interface Grand_prix_piloteRepository extends JpaRepository<Grand_prix_pilote, Integer> {
    
    @Query(nativeQuery = true, value = """
        SELECT * FROM grand_prix_pilotes where etat_grand_prix_pilote>=0    
            """)
    public Page<Grand_prix_pilote> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
        SELECT * FROM grand_prix_pilotes where etat_grand_prix_pilote>=0    
            """)
    public List<Grand_prix_pilote> findAll();

    @Query(nativeQuery = true, value = """
            UPDATE grand_prix_pilotes set etat_grand_prix_pilote= -10 where id_grand_prix_pilote= :id RETURNING *
            """)
    public Grand_prix_pilote delete(int id);


    @Modifying
    @Query(nativeQuery = true, value = """
        INSERT INTO grand_prix_pilotes (idgrandprix,idpilote, temps_course,date_grand_prix)
        SELECT gp.id_grand_prix, p.id_pilote, CAST(temps as TIME ), CAST(dategrandprix as DATE) FROM csv c 
        JOIN pilotes p ON p.nom_pilote = c.nom AND p.date_naissance = CAST(c.datenaissance as date)
        JOIN grand_prixs gp ON gp.nom_grand_prix = c.grandprix
            """)
    public void insertGrandPrixPilote();
}