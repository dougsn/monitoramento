package com.monitoramento.repository.relatorio;


import com.monitoramento.model.relatorio.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {
    @Transactional(readOnly = true)
    Boolean existsByDia(LocalDate dia);

    @Transactional(readOnly = true)
    Optional<Relatorio> findByDia(LocalDate dia);
}