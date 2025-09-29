// src/main/java/com/marsa/authservice/service/TerminalService.java
package com.marsa.authservice.service;

import com.marsa.authservice.model.Terminal;
import com.marsa.authservice.repo.TerminalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TerminalService {
  private final TerminalRepository repo;
  public TerminalService(TerminalRepository repo) { this.repo = repo; }

  public List<Terminal> findAll() { return repo.findAll(); }
  public Optional<Terminal> find(String code) { return repo.findById(code); }
  public Terminal save(Terminal t) { return repo.save(t); }
  public void delete(String code) { repo.deleteById(code); }
}
