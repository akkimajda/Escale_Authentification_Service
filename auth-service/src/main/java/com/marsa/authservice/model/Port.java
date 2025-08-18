package com.marsa.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ports")
public class Port {

  @Id
  @Column(length = 5, nullable = false, unique = true)
  private String codePort;

  @Column(nullable = false)
  private String nomPort;

  private String localisation;

  @Column(nullable = false)
  private int capacite;

  public String getCodePort() { return codePort; }
  public void setCodePort(String codePort) { this.codePort = codePort; }
  public String getNomPort() { return nomPort; }
  public void setNomPort(String nomPort) { this.nomPort = nomPort; }
  public String getLocalisation() { return localisation; }
  public void setLocalisation(String localisation) { this.localisation = localisation; }
  public int getCapacite() { return capacite; }
  public void setCapacite(int capacite) { this.capacite = capacite; }
}
