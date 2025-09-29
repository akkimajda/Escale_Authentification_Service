package com.marsa.authservice.dto;

import java.time.LocalDate;

public class UpsertDapRequest {
    private String navireImo;
    private String terminalCode;
    private Long agentId;
    private LocalDate eta;
    private LocalDate etd;
    private String portLoad;
    private String portUnload;
    private LocalDate dateDap; // optionnel

    // getters/setters
    public String getNavireImo() { return navireImo; }
    public void setNavireImo(String navireImo) { this.navireImo = navireImo; }
    public String getTerminalCode() { return terminalCode; }
    public void setTerminalCode(String terminalCode) { this.terminalCode = terminalCode; }
    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }
    public LocalDate getEta() { return eta; }
    public void setEta(LocalDate eta) { this.eta = eta; }
    public LocalDate getEtd() { return etd; }
    public void setEtd(LocalDate etd) { this.etd = etd; }
    public String getPortLoad() { return portLoad; }
    public void setPortLoad(String portLoad) { this.portLoad = portLoad; }
    public String getPortUnload() { return portUnload; }
    public void setPortUnload(String portUnload) { this.portUnload = portUnload; }
    public LocalDate getDateDap() { return dateDap; }
    public void setDateDap(LocalDate dateDap) { this.dateDap = dateDap; }
}
