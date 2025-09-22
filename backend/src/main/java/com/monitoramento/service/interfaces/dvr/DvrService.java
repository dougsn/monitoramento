package com.monitoramento.service.interfaces.dvr;

import com.monitoramento.dto.dvr.AddDvr;
import com.monitoramento.dto.dvr.AllDvr;
import com.monitoramento.dto.dvr.UpdateDvr;
import com.monitoramento.dto.dvr.ViewDvr;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface DvrService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllDvr>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    ViewDvr findById(Long id);

    @Transactional
    ViewDvr add(AddDvr data);

    @Transactional
    ViewDvr update(UpdateDvr data);

    @Transactional
    Boolean delete(Long id);

}
