package com.marsa.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "navires")
public class Navire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_navire")
    private Long idNavire;

    // 7 chiffres pour l’IMO (ex : 9431985)
    @Column(name = "num_imo", nullable = false, unique = true, length = 7)
    private String numImo;

    @Column(name = "libelle", length = 255)
    private String libelle;

    @Column(name = "longueur")
    private Double longueur;

    @Column(name = "tirant_eau_max")
    private Double tirantEauMax;

    public Navire() {}

    public Long getIdNavire() { return idNavire; }
    public void setIdNavire(Long idNavire) { this.idNavire = idNavire; }

    public String getNumImo() { return numImo; }
    public void setNumImo(String numImo) { this.numImo = numImo; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public Double getLongueur() { return longueur; }
    public void setLongueur(Double longueur) { this.longueur = longueur; }

    public Double getTirantEauMax() { return tirantEauMax; }
    public void setTirantEauMax(Double tirantEauMax) { this.tirantEauMax = tirantEauMax; }
}
