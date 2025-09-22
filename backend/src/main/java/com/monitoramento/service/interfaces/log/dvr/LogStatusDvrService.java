package com.monitoramento.service.interfaces.log.dvr;

import com.monitoramento.dto.log.dvr.AddLogStatusDvr;
import com.monitoramento.dto.log.dvr.AllLogStatusDvr;
import com.monitoramento.dto.log.dvr.ViewLogStatusDvr;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface LogStatusDvrService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllLogStatusDvr>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllLogStatusDvr>> findAllByDvrId(Pageable pageable, Long dvrId);

    @Transactional(readOnly = true)
    ViewLogStatusDvr findById(Long id);

    @Transactional
    ViewLogStatusDvr add(AddLogStatusDvr data);

    @Transactional
    void deleteAllByDvr(Long dvrId);
}
