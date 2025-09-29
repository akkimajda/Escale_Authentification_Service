package com.marsa.authservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Numéro AD/visite : AAAAMMNNN (9 chiffres) */
    @Column(length = 16, unique = true, nullable = false)
    private String adNumber;

    /** Références entités sélectionnées à la création AD */
    @Column(length = 7)
    private String navireImo;

    @Column(length = 32)
    private String terminalCode;

    private Long agentId;

    /** Dates principales */
    private LocalDate dateAd;
    private LocalDate eta;
    private LocalDate etd;

    /** Ports (codes libres/normalisés suivant tes référentiels) */
    @Column(length = 64)
    private String portLoad;

    @Column(length = 64)
    private String portUnload;

    /** Statut du cycle de vie */
    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private VisitStatus statut = VisitStatus.PREVU;

    /** Indicateur DAP (déclaration) */
    @Column(nullable = false)
    private boolean hasDap = false;

    /** Relation 1–1 avec DAP (cascade + orphanRemoval pour supprimer automatiquement) */
    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Dap dap;

    // ----- Getters / Setters -----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdNumber() { return adNumber; }
    public void setAdNumber(String adNumber) { this.adNumber = adNumber; }

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

    public VisitStatus getStatut() { return statut; }
    public void setStatut(VisitStatus statut) { this.statut = statut; }

    public boolean isHasDap() { return hasDap; }
    public void setHasDap(boolean hasDap) { this.hasDap = hasDap; }

    public Dap getDap() { return dap; }
    public void setDap(Dap dap) {
        this.dap = dap;
        // maintien automatique de l'indicateur
        this.hasDap = (dap != null);
    }

    /** Helper : copie les champs “haut de page” depuis un DAP (utile après création/MAJ DAP) */
    public void syncFromDap(Dap d) {
        if (d == null) return;
        this.setNavireImo(d.getNavireImo());
        this.setTerminalCode(d.getTerminalCode());
        this.setAgentId(d.getAgentId());
        this.setEta(d.getEta());
        this.setEtd(d.getEtd());
        this.setPortLoad(d.getPortLoad());
        this.setPortUnload(d.getPortUnload());
    }
}
