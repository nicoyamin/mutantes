package com.challenge.mutantes.repository;

import com.challenge.mutantes.entity.Human;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends JpaRepository<Human, Long> {

    @Query("SELECT h.isMutant FROM Human h where h.dna = :dna")
    Boolean findHumanByDna (@Param("dna") String[] dna);
}
