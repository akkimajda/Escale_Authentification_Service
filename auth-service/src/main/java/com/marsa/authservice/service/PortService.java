package com.marsa.authservice.service;

import com.marsa.authservice.model.Port;
import com.marsa.authservice.repo.PortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortService {
    private final PortRepository repo;

    public PortService(PortRepository repo) {
        this.repo = repo;
    }

    public boolean exists(String code) {
        return repo.existsById(code);
    }

    public Port create(Port p) {
        return repo.save(p);
    }

    public List<Port> search(String code, String nom, String loc) {
        if (code != null && !code.isBlank()) return repo.findByCodePortIgnoreCase(code);
        if (nom  != null && !nom.isBlank())  return repo.findByNomPortContainingIgnoreCase(nom);
        if (loc  != null && !loc.isBlank())  return repo.findByLocalisationContainingIgnoreCase(loc);
        return repo.findAll();
    }

    public Port update(String code, Port data) {
        Port p = repo.findById(code).orElseThrow();
        p.setNomPort(data.getNomPort());
        p.setLocalisation(data.getLocalisation());
        p.setCapacite(data.getCapacite());
        return repo.save(p);
    }

    public void delete(String code) {
        repo.deleteById(code);
    }
}
