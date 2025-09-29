package com.marsa.authservice.dto;

import java.time.LocalDate;

public class UpsertDapResponse {
    private Long id;
    private Long visitId;
    private LocalDate dateDap;
    private LocalDate eta;

    public UpsertDapResponse(Long id, Long visitId, LocalDate dateDap, LocalDate eta) {
        this.id = id; this.visitId = visitId; this.dateDap = dateDap; this.eta = eta;
    }
    public Long getId() { return id; }
    public Long getVisitId() { return visitId; }
    public LocalDate getDateDap() { return dateDap; }
    public LocalDate getEta() { return eta; }
}
