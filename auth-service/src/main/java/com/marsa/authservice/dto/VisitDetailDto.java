package com.marsa.authservice.dto;

import com.marsa.authservice.model.VisitStatus;
import java.time.LocalDate;

public class VisitDetailDto {
  public Long id;
  public String adNumber;
  public String navireImo;
  public String terminalCode;
  public Long agentId;
  public LocalDate dateAd;
  public LocalDate eta;
  public LocalDate etd;
  public String portLoad;
  public String portUnload;
  public VisitStatus statut;
  public boolean hasDap;
}
