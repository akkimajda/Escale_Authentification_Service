package com.marsa.authservice.controller;

import com.marsa.authservice.model.Agent;
import com.marsa.authservice.repo.AgentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Endpoints REST pour l'entité Agent maritime.
 * - GET     /api/agents            => recherche (autocomplete) ?code=...&nom=...
 * - GET     /api/agents/{id}       => lecture d'un agent
 * - POST    /api/agents            => création { "code": "...", "nom": "..." }
 * - PUT     /api/agents/{id}       => mise à jour
 * - DELETE  /api/agents/{id}       => suppression
 */
@RestController
@RequestMapping("/api/agents")
// @CrossOrigin(origins = "http://localhost:4201") // à décommenter si besoin en dev
public class AgentController {

  private final AgentRepository repo;

  public AgentController(AgentRepository repo) {
    this.repo = repo;
  }

  /** Recherche/autocomplete: code et/ou nom (top 20). */
  @GetMapping
  public List<Map<String, Object>> search(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String nom) {

    String c = Optional.ofNullable(code).orElse("");
    String n = Optional.ofNullable(nom).orElse("");

    List<Agent> list;
    if (c.isEmpty() && n.isEmpty()) {
      list = repo.findAll();
    } else {
      list = repo.findTop20ByCodeAgentContainingIgnoreCaseOrNomAgentContainingIgnoreCase(c, n);
    }

    List<Map<String, Object>> out = new ArrayList<>(list.size());
    for (Agent a : list) {
      out.add(Map.of(
          "id",   a.getIdAgent(),
          "code", a.getCodeAgent(),
          "nom",  a.getNomAgent()
      ));
    }
    return out;
  }

  /** Lecture d'un agent — retourne ResponseEntity<?> pour éviter le mismatch de types. */
  @GetMapping("/{id}")
  public ResponseEntity<?> getOne(@PathVariable Long id) {
    return repo.findById(id)
        .map(a -> ResponseEntity.ok(Map.of(
            "id",   a.getIdAgent(),
            "code", a.getCodeAgent(),
            "nom",  a.getNomAgent()
        )))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /** Création: POST JSON { "code": "...", "nom": "..." } */
  @PostMapping
  public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
    String code = Optional.ofNullable(body.get("code")).orElse("").trim();
    String nom  = Optional.ofNullable(body.get("nom")).orElse("").trim();

    if (code.isEmpty() || nom.isEmpty()) {
      return ResponseEntity.badRequest().body("Champs 'code' et 'nom' requis");
    }
    if (repo.existsByCodeAgentIgnoreCase(code)) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("Ce code agent existe déjà");
    }

    Agent a = new Agent();
    a.setCodeAgent(code);
    a.setNomAgent(nom);
    a = repo.save(a);

    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
        "id",   a.getIdAgent(),
        "code", a.getCodeAgent(),
        "nom",  a.getNomAgent()
    ));
  }

  /** Mise à jour. */
  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
    return repo.findById(id).map(a -> {
      String code = Optional.ofNullable(body.get("code")).orElse("").trim();
      String nom  = Optional.ofNullable(body.get("nom")).orElse("").trim();

      if (code.isEmpty() || nom.isEmpty()) {
        return ResponseEntity.badRequest().body("Champs 'code' et 'nom' requis");
      }
      if (!a.getCodeAgent().equalsIgnoreCase(code) && repo.existsByCodeAgentIgnoreCase(code)) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Ce code agent existe déjà");
      }

      a.setCodeAgent(code);
      a.setNomAgent(nom);
      Agent saved = repo.save(a);

      return ResponseEntity.ok(Map.of(
          "id",   saved.getIdAgent(),
          "code", saved.getCodeAgent(),
          "nom",  saved.getNomAgent()
      ));
    }).orElseGet(() -> ResponseEntity.notFound().build());
  }

  /** Suppression. */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (!repo.existsById(id)) return ResponseEntity.notFound().build();
    repo.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
