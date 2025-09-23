package com.monitoramento.repository.dvr_relatorio;


import com.monitoramento.model.dvr_relatorio.DvrRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DvrRelatorioRepository extends JpaRepository<DvrRelatorio, Long> {
    @Transactional(readOnly = true)
    Boolean existsByDia(LocalDate dia);
    @Transactional(readOnly = true)
    Boolean existsByDiaAndDvrId(LocalDate dia, Long dvrId);
    @Transactional(readOnly = true)
    Optional<DvrRelatorio> findByDiaAndDvrId(LocalDate dia, Long dvrId);

    @Transactional(readOnly = true)
    List<DvrRelatorio> findAllByDia(LocalDate dia);
}