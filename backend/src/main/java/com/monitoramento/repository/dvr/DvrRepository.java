package com.monitoramento.repository.dvr;


import com.monitoramento.model.dvr.Dvr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface DvrRepository extends JpaRepository<Dvr, Long> {
    @Transactional(readOnly = true)
    Boolean existsByNome(String nome);

    @Transactional(readOnly = true)
    Optional<Dvr> findByNome(String nome);
}