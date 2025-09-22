package com.monitoramento.service.interfaces.status;

import com.monitoramento.dto.status.AddStatus;
import com.monitoramento.dto.status.AllStatus;
import com.monitoramento.dto.status.UpdateStatus;
import com.monitoramento.dto.status.ViewStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface StatusService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllStatus>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    ViewStatus findById(Long id);

    @Transactional
    ViewStatus add(AddStatus data);

    @Transactional
    ViewStatus update(UpdateStatus data);

    @Transactional
    Boolean delete(Long id);

}
