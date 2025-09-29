package com.marsa.authservice.repo;

import com.marsa.authservice.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long> {

  // Autocomplete: TOP 20 par code ou nom (insensible à la casse)
  List<Agent> findTop20ByCodeAgentContainingIgnoreCaseOrNomAgentContainingIgnoreCase(
      String codeAgent, String nomAgent);

  // Unicité du code
  boolean existsByCodeAgentIgnoreCase(String codeAgent);
}
