package com.marsa.authservice.controller;

import com.marsa.authservice.model.Navire;
import com.marsa.authservice.repo.NavireRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/navires")
@CrossOrigin(origins = "http://localhost:4201")
public class NavireController {

    private final NavireRepository repo;

    public NavireController(NavireRepository repo) {
        this.repo = repo;
    }

    // GET /api/navires?num_imo=&libelle=
    @GetMapping
    public List<Navire> list(@RequestParam(name = "num_imo", required = false) String numImo,
                             @RequestParam(name = "libelle", required = false) String libelle) {
        if ((numImo == null || numImo.isBlank()) && (libelle == null || libelle.isBlank())) {
            return repo.findAll();
        }

        Navire probe = new Navire();
        if (numImo != null && !numImo.isBlank()) probe.setNumImo(numImo);
        if (libelle != null && !libelle.isBlank()) probe.setLibelle(libelle);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        return repo.findAll(Example.of(probe, matcher));
    }

    // GET /api/navires/{id}
    @GetMapping("/{id}")
    public Navire get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Navire introuvable"));
    }

    // POST /api/navires
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Navire n) {
        if (n.getNumImo() == null || !n.getNumImo().matches("^\\d{7}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "num_imo invalide (7 chiffres)");
        }
        if (repo.existsByNumImo(n.getNumImo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "num_imo déjà existant");
        }
        repo.save(n);
        return ResponseEntity.ok("Navire créé");
    }

    // PUT /api/navires/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Navire n) {
        Navire db = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Navire introuvable"));

        if (n.getNumImo() != null && !n.getNumImo().equals(db.getNumImo())) {
            if (!n.getNumImo().matches("^\\d{7}$")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "num_imo invalide (7 chiffres)");
            }
            if (repo.existsByNumImo(n.getNumImo())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "num_imo déjà existant");
            }
            db.setNumImo(n.getNumImo());
        }

        if (n.getLibelle() != null)      db.setLibelle(n.getLibelle());
        if (n.getLongueur() != null)     db.setLongueur(n.getLongueur());
        if (n.getTirantEauMax() != null) db.setTirantEauMax(n.getTirantEauMax());

        repo.save(db);
        return ResponseEntity.ok("Navire modifié");
    }

    // DELETE /api/navires/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Navire introuvable");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Navire supprimé");
    }
}
