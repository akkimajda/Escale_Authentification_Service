package com.marsa.authservice.service;

import com.marsa.authservice.dto.CreateVisitRequest;
import com.marsa.authservice.dto.UpsertDapRequest;
import com.marsa.authservice.model.Dap;
import com.marsa.authservice.model.Visit;
import com.marsa.authservice.model.VisitStatus;
import com.marsa.authservice.repo.DapRepository;
import com.marsa.authservice.repo.VisitRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Service
public class VisitService {

    private final VisitRepository repo;
    private final DapRepository dapRepo;

    private static final DateTimeFormatter YM = DateTimeFormatter.ofPattern("yyyyMM");

    public VisitService(VisitRepository repo, DapRepository dapRepo) {
        this.repo = repo;
        this.dapRepo = dapRepo;
    }

    /* === Création AD === */
    @Transactional
    public Visit createFromRequest(CreateVisitRequest req) {
        Visit v = new Visit();
        v.setNavireImo(req.getNavireImo());
        v.setTerminalCode(req.getTerminalCode());
        v.setAgentId(req.getAgentId());
        v.setDateAd(req.getDateAd());
        v.setEta(req.getEta());
        v.setEtd(req.getEtd());
        v.setPortLoad(req.getPortLoad());
        v.setPortUnload(req.getPortUnload());
        if (v.getStatut() == null) v.setStatut(VisitStatus.PREVU);

        String ym = LocalDate.now().format(YM);
        v.setAdNumber(nextNumberFor(ym));

        try {
            return repo.save(v);
        } catch (DataIntegrityViolationException e) {
            v.setAdNumber(nextNumberFor(ym));
            return repo.save(v);
        }
    }

    private String nextNumberFor(String ym) {
        Integer maxSeq = repo.findMaxSeqForYearMonth(ym);
        int next = (maxSeq == null ? 0 : maxSeq) + 1;
        if (next > 999) throw new IllegalStateException("Quota mensuel de visites dépassé");
        return ym + String.format("%03d", next);
    }

    /* === DAP === */

    @Transactional
    public Dap upsertDap(Long visitId, UpsertDapRequest req) {
        Visit v = repo.findById(visitId)
                .orElseThrow(() -> new IllegalArgumentException("Visite introuvable: " + visitId));

        Dap d = dapRepo.findByVisitId(visitId).orElseGet(() -> {
            Dap nd = new Dap();
            nd.setVisit(v);
            return nd;
        });

        d.setDateDap(req.getDateDap() != null ? req.getDateDap() : LocalDate.now());
        d.setEta(req.getEta());
        d.setEtd(req.getEtd());
        d.setNavireImo(req.getNavireImo());
        d.setTerminalCode(req.getTerminalCode());
        d.setAgentId(req.getAgentId());
        d.setPortLoad(req.getPortLoad());
        d.setPortUnload(req.getPortUnload());

        // synchroniser la visite
        v.setNavireImo(req.getNavireImo());
        v.setTerminalCode(req.getTerminalCode());
        v.setAgentId(req.getAgentId());
        v.setEta(req.getEta());
        v.setEtd(req.getEtd());
        v.setPortLoad(req.getPortLoad());
        v.setPortUnload(req.getPortUnload());
        v.setHasDap(true);
        v.setStatut(VisitStatus.VALIDE);

        dapRepo.save(d);
        repo.save(v);
        return d;
    }

    /** Supprime le DAP (DELETE direct) et remet la visite à PREVU + hasDap=false */
    @Transactional
    public Visit deleteDap(Long visitId) {
        Visit v = repo.findById(visitId)
                .orElseThrow(() -> new NoSuchElementException("Visite introuvable: " + visitId));

        // suppression SQL directe (pas de setVisit(null))
        dapRepo.hardDeleteByVisitId(visitId);

        v.setHasDap(false);
        v.setStatut(VisitStatus.PREVU);
        return repo.save(v);
    }

    @Transactional
    public Visit setStatus(Long visitId, VisitStatus statut) {
        Visit v = repo.findById(visitId)
                .orElseThrow(() -> new NoSuchElementException("Visite introuvable: " + visitId));
        v.setStatut(statut);
        return repo.save(v);
    }
}
