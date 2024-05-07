package com.evaluation.Eval.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.evaluation.Eval.entity.Utilisateur;

import java.util.List;



@Repository
public interface UserRepository extends JpaRepository<Utilisateur, Integer> {
    

    Optional<Utilisateur> findByEmail(String email);


    @Query(nativeQuery = true, value ="""
            SELECT * FROM users where email= :email and password=:password and etat_user =0 
            """)
    Optional<Utilisateur> findByEmailAndPassword(String email, String password);

}
