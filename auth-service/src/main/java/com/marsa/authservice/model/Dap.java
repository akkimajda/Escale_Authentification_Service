package com.marsa.authservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dap")
public class Dap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Relation 1–1 avec la visite */
    @OneToOne(optional = false)
    @JoinColumn(name = "visit_id", nullable = false, unique = true)
    private Visit visit;

    private LocalDate dateDap;      // laissé libre (peut être null => on mettra today au service)
    private LocalDate eta;
    private LocalDate etd;

    @Column(length = 20)  private String navireImo;
    @Column(length = 32)  private String terminalCode;
    private Long agentId;

    @Column(length = 64)  private String portLoad;
    @Column(length = 64)  private String portUnload;

    // --- getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }

    public LocalDate getDateDap() { return dateDap; }
    public void setDateDap(LocalDate dateDap) { this.dateDap = dateDap; }

    public LocalDate getEta() { return eta; }
    public void setEta(LocalDate eta) { this.eta = eta; }

    public LocalDate getEtd() { return etd; }
    public void setEtd(LocalDate etd) { this.etd = etd; }

    public String getNavireImo() { return navireImo; }
    public void setNavireImo(String navireImo) { this.navireImo = navireImo; }

    public String getTerminalCode() { return terminalCode; }
    public void setTerminalCode(String terminalCode) { this.terminalCode = terminalCode; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    public String getPortLoad() { return portLoad; }
    public void setPortLoad(String portLoad) { this.portLoad = portLoad; }

    public String getPortUnload() { return portUnload; }
    public void setPortUnload(String portUnload) { this.portUnload = portUnload; }
}
