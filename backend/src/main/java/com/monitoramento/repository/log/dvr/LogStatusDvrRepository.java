package com.monitoramento.repository.log.dvr;


import com.monitoramento.model.log.LogStatusDvr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LogStatusDvrRepository extends JpaRepository<LogStatusDvr, Long> {

    @Transactional(readOnly = true)
    List<LogStatusDvr> findAllByDvrId(Long dvrId);

    @Transactional(readOnly = true)
    Page<LogStatusDvr> findAllByDvrId(Long dvrId, Pageable pageable);
}