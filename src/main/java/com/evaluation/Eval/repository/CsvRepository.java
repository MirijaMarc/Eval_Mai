package com.evaluation.Eval.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.evaluation.Eval.util.Csv;

@Repository
public interface CsvRepository extends JpaRepository<Csv,Integer>{

    
    
}
