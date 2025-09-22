package com.monitoramento.repository.status;


import com.monitoramento.model.status.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    @Transactional(readOnly = true)
    Boolean existsByNome(String nome);

    @Transactional(readOnly = true)
    Boolean existsByCor(String cor);

    @Transactional(readOnly = true)
    Boolean existsByCorOrNome(String cor, String nome);

    @Transactional(readOnly = true)
    Optional<Status> findByNome(String nome);

    @Transactional(readOnly = true)
    Optional<Status> findByCor(String cor);
}