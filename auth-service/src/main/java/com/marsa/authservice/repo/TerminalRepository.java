package com.marsa.authservice.repo;

import com.marsa.authservice.model.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerminalRepository extends JpaRepository<Terminal, String> {}
