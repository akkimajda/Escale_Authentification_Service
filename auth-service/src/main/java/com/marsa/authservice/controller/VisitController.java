// src/main/java/com/marsa/authservice/controller/VisitController.java
package com.marsa.authservice.controller;

import com.marsa.authservice.dto.CreateVisitRequest;
import com.marsa.authservice.dto.UpsertDapRequest;
import com.marsa.authservice.dto.UpsertDapResponse;
import com.marsa.authservice.dto.VisitDetailDto;
import com.marsa.authservice.dto.VisitDetailDto.DapDto;
import com.marsa.authservice.model.Dap;
import com.marsa.authservice.model.Visit;
import com.marsa.authservice.model.VisitStatus;
import com.marsa.authservice.repo.DapRepository;
import com.marsa.authservice.repo.VisitRepository;
import com.marsa.authservice.service.VisitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "http://localhost:4201")
public class VisitController {

    private final VisitRepository repo;
    private final VisitService service;
    private final DapRepository dapRepo;

    public VisitController(VisitRepository repo, VisitService service, DapRepository dapRepo) {
        this.repo = repo;
        this.service = service;
        this.dapRepo = dapRepo;
    }

    private static String normalizeNumber(String s) {
        return (s == null) ? null : s.replaceAll("\\D", "");
    }

    /* ===================== AD ===================== */

    /** Création AD (= création d'une visite) */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody CreateVisitRequest req) {
        Visit v = service.createFromRequest(req);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("id", v.getId());
        body.put("adNumber", v.getAdNumber());
        body.put("dateAd", v.getDateAd());
        body.put("eta", v.getEta());
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /** Prochain numéro si tu veux l’afficher côté UI (placeholder) */
    @GetMapping("/next-ad")
    public Map<String, String> nextAd() {
        return Map.of("adNumber", "preview-only");
    }

    /** Récupération d'une visite par id numérique OU par numéro AD (>=9 chiffres) */
    @GetMapping("/{idOrNumber}")
    public ResponseEntity<VisitDetailDto> getOne(@PathVariable String idOrNumber) {
        // 1) ressemble à un numéro AD (ex: 202509006)
        if (idOrNumber.matches("\\d{9,}")) {
            return repo.findByAdNumber(idOrNumber)
                    .map(this::toDetail)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        // 2) sinon, essaye comme id
        try {
            long id = Long.parseLong(idOrNumber);
            return repo.findById(id)
                    .map(this::toDetail)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (NumberFormatException nfe) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-ad/{adNumber}")
    public ResponseEntity<Visit> getByAd(@PathVariable String adNumber) {
        return repo.findByAdNumber(normalizeNumber(adNumber))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Recherche multi-filtres (pagination + tri) */
    @GetMapping
    public ResponseEntity<Page<Map<String, Object>>> search(
            @RequestParam(required = false) String visitNumber,
            @RequestParam(required = false) String adNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate etaFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate etaTo,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate etdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate etdTo,
            @RequestParam(required = false) List<VisitStatus> statuses,
            @RequestParam(required = false) Long agentId,
            @RequestParam(required = false) String navireImo,
            @RequestParam(required = false) Boolean hasDap,
            @PageableDefault(size = 20, sort = "adNumber", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        String number = normalizeNumber(
                (visitNumber != null && !visitNumber.isBlank()) ? visitNumber : adNumber
        );

        Specification<Visit> spec = Specification.where(null);

        if (number != null && !number.isBlank())
            spec = spec.and((root, q, cb) -> cb.equal(root.get("adNumber"), number));

        if (etaFrom != null) spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("eta"), etaFrom));
        if (etaTo   != null) spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("eta"), etaTo));

        if (etdFrom != null) spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("etd"), etdFrom));
        if (etdTo   != null) spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("etd"), etdTo));

        if (statuses != null && !statuses.isEmpty())
            spec = spec.and((r, q, cb) -> r.get("statut").in(statuses));

        if (agentId != null)
            spec = spec.and((r, q, cb) -> cb.equal(r.get("agentId"), agentId));

        if (navireImo != null && !navireImo.isBlank())
            spec = spec.and((r, q, cb) -> cb.equal(r.get("navireImo"), navireImo));

        if (hasDap != null)
            spec = spec.and((r, q, cb) -> cb.equal(r.get("hasDap"), hasDap));

        Page<Visit> page = repo.findAll(spec, pageable);

        Page<Map<String, Object>> dto = page.map(v -> {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id",           v.getId());
            m.put("visitNumber",  v.getAdNumber()); // alias
            m.put("agentId",      v.getAgentId());
            m.put("navireImo",    v.getNavireImo());
            m.put("eta",          v.getEta());
            m.put("etd",          v.getEtd());
            m.put("statut",       v.getStatut());
            return m;
        });

        return ResponseEntity.ok(dto);
    }

    /* ===================== DAP ===================== */

    /** Création (ou première sauvegarde) du DAP pour une visite */
    @PostMapping("/{id}/dap")
    public ResponseEntity<UpsertDapResponse> createDap(@PathVariable Long id,
                                                       @RequestBody UpsertDapRequest req) {
        boolean creating = dapRepo.findByVisitId(id).isEmpty();
        Dap d = service.upsertDap(id, req);
        UpsertDapResponse body = new UpsertDapResponse(d.getId(), id, d.getDateDap(), d.getEta());
        return ResponseEntity.status(creating ? HttpStatus.CREATED : HttpStatus.OK).body(body);
    }

    /** Mise à jour du DAP */
    @PutMapping("/{id}/dap")
    public ResponseEntity<UpsertDapResponse> updateDap(@PathVariable Long id,
                                                       @RequestBody UpsertDapRequest req) {
        Dap d = service.upsertDap(id, req);
        return ResponseEntity.ok(new UpsertDapResponse(d.getId(), id, d.getDateDap(), d.getEta()));
    }

    /** Suppression du DAP : on renvoie la visite mise à jour (hasDap=false, statut=PREVU) */
    @DeleteMapping("/{id}/dap")
    public ResponseEntity<VisitDetailDto> deleteDap(@PathVariable Long id) {
        Visit v = service.deleteDap(id);
        return ResponseEntity.ok(toDetail(v));
    }

    /** (optionnel) Récupérer le DAP de la visite */
    @GetMapping("/{id}/dap")
    public ResponseEntity<Dap> getDap(@PathVariable Long id) {
        return dapRepo.findByVisitId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /* ===================== Statut ===================== */

    /** Changer le statut (PREVU, VALIDE, ACTIVE, CLOTURE, ANNULE) */
    @PutMapping("/{id}/status")
    public ResponseEntity<VisitDetailDto> setStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        VisitStatus s = VisitStatus.valueOf(body.get("statut"));
        Visit v = service.setStatus(id, s);
        return ResponseEntity.ok(toDetail(v));
    }

    /* ===================== Mapper entité -> DTO ===================== */

    private VisitDetailDto toDetail(Visit v) {
        VisitDetailDto d = new VisitDetailDto();
        d.id = v.getId();
        d.adNumber = v.getAdNumber();
        d.navireImo = v.getNavireImo();
        d.terminalCode = v.getTerminalCode();
        d.agentId = v.getAgentId();
        d.dateAd = v.getDateAd();
        d.eta = v.getEta();
        d.etd = v.getEtd();
        d.portLoad = v.getPortLoad();
        d.portUnload = v.getPortUnload();
        d.statut = v.getStatut();
        d.hasDap = v.isHasDap();

        // Peupler le bloc DAP si existant
        dapRepo.findByVisitId(v.getId()).ifPresent(dap -> {
            DapDto dd = new DapDto();
            dd.id = dap.getId();
            dd.dateDap = dap.getDateDap();
            dd.eta = dap.getEta();
            dd.etd = dap.getEtd();
            dd.navireImo = dap.getNavireImo();
            dd.terminalCode = dap.getTerminalCode();
            dd.agentId = dap.getAgentId();
            dd.portLoad = dap.getPortLoad();
            dd.portUnload = dap.getPortUnload();
            d.dap = dd;
        });

        return d;
    }
}
