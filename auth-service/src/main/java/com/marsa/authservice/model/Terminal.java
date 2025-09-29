package com.marsa.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "terminals")
public class Terminal {

    @Id
    @Column(length = 5, nullable = false, unique = true)
    private String codeTerminal;

    private String nomTerminal;
    private String localisation;
    private Integer capacite;

    public Terminal() {}

    public Terminal(String codeTerminal, String nomTerminal, String localisation, Integer capacite) {
        this.codeTerminal = codeTerminal;
        this.nomTerminal = nomTerminal;
        this.localisation = localisation;
        this.capacite = capacite;
    }

    public String getCodeTerminal() { return codeTerminal; }
    public void setCodeTerminal(String codeTerminal) { this.codeTerminal = codeTerminal; }
    public String getNomTerminal() { return nomTerminal; }
    public void setNomTerminal(String nomTerminal) { this.nomTerminal = nomTerminal; }
    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }
}
