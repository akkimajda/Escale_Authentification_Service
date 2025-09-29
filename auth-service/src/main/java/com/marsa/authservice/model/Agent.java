package com.marsa.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agents")
public class Agent {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_agent")
  private Long idAgent;

  @Column(name = "code_agent", nullable = false, unique = true, length = 50)
  private String codeAgent;

  @Column(name = "nom_agent", nullable = false, length = 255)
  private String nomAgent;

  // Getters/Setters
  public Long getIdAgent() { return idAgent; }
  public void setIdAgent(Long idAgent) { this.idAgent = idAgent; }
  public String getCodeAgent() { return codeAgent; }
  public void setCodeAgent(String codeAgent) { this.codeAgent = codeAgent; }
  public String getNomAgent() { return nomAgent; }
  public void setNomAgent(String nomAgent) { this.nomAgent = nomAgent; }
}
