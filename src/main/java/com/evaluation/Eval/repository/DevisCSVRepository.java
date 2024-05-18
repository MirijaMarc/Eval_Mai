package com.evaluation.Eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evaluation.Eval.entity.DevisCSV;


@Repository
public interface DevisCSVRepository extends JpaRepository<DevisCSV, Integer>{
    
}
