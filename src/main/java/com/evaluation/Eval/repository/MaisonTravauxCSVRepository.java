package com.evaluation.Eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evaluation.Eval.entity.MaisonsTravauxCSV;


@Repository
public interface MaisonTravauxCSVRepository extends JpaRepository<MaisonsTravauxCSV, Integer> {
    
}
