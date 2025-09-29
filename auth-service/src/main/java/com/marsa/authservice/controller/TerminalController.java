package com.marsa.authservice.controller;

import com.marsa.authservice.model.Terminal;
import com.marsa.authservice.service.TerminalService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terminals")
@CrossOrigin(origins = {
        "http://localhost:4200", "http://127.0.0.1:4200",
        "http://localhost:4201", "http://127.0.0.1:4201",
        "http://localhost:61021", "http://127.0.0.1:61021"
})
public class TerminalController {

    private final TerminalService service;

    public TerminalController(TerminalService service) {
        this.service = service;
    }

    // LISTE (200 + [] garanti, type JSON explicite)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Terminal>> list(
            @RequestParam(required = false) String codeTerminal,
            @RequestParam(required = false) String nomTerminal,
            @RequestParam(required = false) String localisation
    ) {
        List<Terminal> all = service.findAll();
        List<Terminal> filtres = all.stream()
                .filter(t -> codeTerminal == null || t.getCodeTerminal().contains(codeTerminal))
                .filter(t -> nomTerminal == null || (t.getNomTerminal() != null &&
                        t.getNomTerminal().toLowerCase().contains(nomTerminal.toLowerCase())))
                .filter(t -> localisation == null || (t.getLocalisation() != null &&
                        t.getLocalisation().toLowerCase().contains(localisation.toLowerCase())))
                .toList();
        return ResponseEntity.ok(filtres);
    }

    // GET ONE
    @GetMapping(value = "/{code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Terminal> getOne(@PathVariable String code) {
        return service.find(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> create(@RequestBody Terminal t) {
        if (t.getCodeTerminal() == null || !t.getCodeTerminal().matches("^[A-Z]{5}$")) {
            return ResponseEntity.badRequest().body("invalid codeTerminal");
        }
        if (service.find(t.getCodeTerminal()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("exists");
        }
        service.save(t);
        return ResponseEntity.ok("OK");
    }

    // UPDATE
    @PutMapping(value = "/{code}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> update(@PathVariable String code, @RequestBody Terminal t) {
        return service.find(code)
                .map(existing -> {
                    existing.setNomTerminal(t.getNomTerminal());
                    existing.setLocalisation(t.getLocalisation());
                    existing.setCapacite(t.getCapacite());
                    service.save(existing);
                    return ResponseEntity.ok("UPDATED");
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found"));
    }

    // DELETE
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        if (service.find(code).isEmpty()) return ResponseEntity.notFound().build();
        service.delete(code);
        return ResponseEntity.noContent().build();
    }
}
