package com.evaluation.Eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evaluation.Eval.entity.PaiementCSV;

@Repository
public interface PaiementCSVRepository extends JpaRepository<PaiementCSV, Integer> {
    
}
