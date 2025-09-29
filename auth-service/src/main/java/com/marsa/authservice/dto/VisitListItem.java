package com.marsa.authservice.dto;

import com.marsa.authservice.model.VisitStatus;
import java.time.LocalDate;

public record VisitListItem(
        Long id,
        String visitNumber,   // = adNumber
        Long agentId,
        String navireImo,
        LocalDate eta,
        LocalDate etd,
        VisitStatus statut
) {}
