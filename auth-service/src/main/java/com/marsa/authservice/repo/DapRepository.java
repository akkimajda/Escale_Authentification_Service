package com.marsa.authservice.repo;

import com.marsa.authservice.model.Dap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DapRepository extends JpaRepository<Dap, Long> {
    Optional<Dap> findByVisitId(Long visitId);
    void deleteByVisitId(Long visitId);
}
