package com.marsa.authservice.repo;

import com.marsa.authservice.model.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortRepository extends JpaRepository<Port, String> {
    List<Port> findByCodePortIgnoreCase(String codePort);
    List<Port> findByNomPortContainingIgnoreCase(String nomPort);
    List<Port> findByLocalisationContainingIgnoreCase(String localisation);
}
