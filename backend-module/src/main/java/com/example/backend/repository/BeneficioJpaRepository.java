package com.example.backend.repository;


import java.util.Optional;

import com.example.backend.domain.Beneficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficioJpaRepository extends JpaRepository<Beneficio, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Beneficio b where b.id = :id")
    Optional<Beneficio> findByIdForUpdate(@Param("id") Long id);
}