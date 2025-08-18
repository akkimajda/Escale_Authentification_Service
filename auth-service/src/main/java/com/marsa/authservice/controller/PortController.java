package com.marsa.authservice.controller;

import com.marsa.authservice.model.Port;
import com.marsa.authservice.service.PortService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4201")
@RestController
@RequestMapping("/api/ports")
public class PortController {

    private final PortService service;

    public PortController(PortService service) {
        this.service = service;
    }

    // CREATE
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> create(@RequestBody Port dto) {
        if (dto.getCodePort() == null || !dto.getCodePort().matches("^[A-Z]{5}$")) {
            return ResponseEntity.badRequest().body("codePort invalide (5 lettres majuscules)");
        }
        if (service.exists(dto.getCodePort())) {
            return ResponseEntity.badRequest().body("codePort déjà existant");
        }
        service.create(dto);
        return ResponseEntity.ok("Port créé");
    }

    // LIST + critères
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Port>> search(
            @RequestParam(required = false) String codePort,
            @RequestParam(required = false) String nomPort,
            @RequestParam(required = false) String localisation
    ) {
        return ResponseEntity.ok(service.search(codePort, nomPort, localisation));
    }

    // UPDATE (sans changer codePort)
    @PutMapping(value = "/{codePort}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Port> update(@PathVariable String codePort, @RequestBody Port data) {
        return ResponseEntity.ok(service.update(codePort, data));
    }

    // DELETE
    @DeleteMapping("/{codePort}")
    public ResponseEntity<Void> delete(@PathVariable String codePort) {
        if (!service.exists(codePort)) return ResponseEntity.notFound().build();
        service.delete(codePort);
        return ResponseEntity.noContent().build();
    }
}
