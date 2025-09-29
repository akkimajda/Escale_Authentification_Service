package com.marsa.authservice.repo;

import com.marsa.authservice.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    Optional<Visit> findTopByAdNumberStartingWithOrderByAdNumberDesc(String prefix);

    Optional<Visit> findByAdNumber(String adNumber);

    // ➜ Ajout pour calculer la séquence max du mois (yyyyMM)
    @Query(value = """
        SELECT COALESCE(MAX(CAST(SUBSTRING(v.ad_number FROM 7 FOR 3) AS INTEGER)), 0)
        FROM visits v
        WHERE SUBSTRING(v.ad_number FROM 1 FOR 6) = :ym
        """, nativeQuery = true)
    Integer findMaxSeqForYearMonth(@Param("ym") String yearMonth);
}
