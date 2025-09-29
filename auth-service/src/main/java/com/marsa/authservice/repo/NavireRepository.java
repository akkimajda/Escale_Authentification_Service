package com.marsa.authservice.repo;

import com.marsa.authservice.model.Navire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NavireRepository extends JpaRepository<Navire, Long> {
    boolean existsByNumImo(String numImo);
    Optional<Navire> findByNumImo(String numImo);
}
