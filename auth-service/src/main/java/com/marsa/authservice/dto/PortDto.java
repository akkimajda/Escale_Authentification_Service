package com.marsa.authservice.dto;

public class PortDto {
    private String codePort;
    private String nomPort;
    private String localisation;
    private int capacite;

    // Getters et setters
    public String getCodePort() { return codePort; }
    public void setCodePort(String codePort) { this.codePort = codePort; }

    public String getNomPort() { return nomPort; }
    public void setNomPort(String nomPort) { this.nomPort = nomPort; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    @Override
    public String toString() {
        return "PortDto{" +
                "codePort='" + codePort + '\'' +
                ", nomPort='" + nomPort + '\'' +
                ", localisation='" + localisation + '\'' +
                ", capacite=" + capacite +
                '}';
    }
}
