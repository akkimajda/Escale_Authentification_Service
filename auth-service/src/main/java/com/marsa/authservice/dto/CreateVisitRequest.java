package com.marsa.authservice.dto;

import java.time.LocalDate;

public class CreateVisitRequest {

    private String navireImo;
    private String terminalCode;
    private Long   agentId;

    private LocalDate dateAd;
    private LocalDate eta;
    private LocalDate etd;       // optionnel

    private String portLoad;
    private String portUnload;

    // Getters / Setters
    public String getNavireImo() { return navireImo; }
    public void setNavireImo(String navireImo) { this.navireImo = navireImo; }

    public String getTerminalCode() { return terminalCode; }
    public void setTerminalCode(String terminalCode) { this.terminalCode = terminalCode; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    public LocalDate getDateAd() { return dateAd; }
    public void setDateAd(LocalDate dateAd) { this.dateAd = dateAd; }

    public LocalDate getEta() { return eta; }
    public void setEta(LocalDate eta) { this.eta = eta; }

    public LocalDate getEtd() { return etd; }
    public void setEtd(LocalDate etd) { this.etd = etd; }

    public String getPortLoad() { return portLoad; }
    public void setPortLoad(String portLoad) { this.portLoad = portLoad; }

    public String getPortUnload() { return portUnload; }
    public void setPortUnload(String portUnload) { this.portUnload = portUnload; }
}
