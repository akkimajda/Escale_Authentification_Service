package com.marsa.authservice.repo;

import com.marsa.authservice.model.Dap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface DapRepository extends JpaRepository<Dap, Long> {

    Optional<Dap> findByVisitId(Long visitId);

    // DELETE direct sans charger l'entité (évite tout update à null)
    @Modifying
    @Transactional
    @Query("delete from Dap d where d.visit.id = :visitId")
    int hardDeleteByVisitId(@Param("visitId") Long visitId);
}
