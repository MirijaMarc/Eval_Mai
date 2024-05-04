package com.evaluation.Eval.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.Data;

@Data
public class DetailEcurie {
    private Ecurie ecurie;
    private ArrayList<Pilote> pilotes;



    public Object getPage(EntityManager entityManager, int idecurie){
        String sql = "select * from v_ecurie_pilotes where id_ecurie = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, idecurie);
        List<Object[]> o = query.getResultList();
        for (Object[] e : o) {
            Object a = e[1];
            System.out.println(a);
        }

        return null;
    }

    
}
