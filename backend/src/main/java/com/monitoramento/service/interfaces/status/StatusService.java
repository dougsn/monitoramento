package com.monitoramento.service.interfaces.status;

import com.monitoramento.dto.status.*;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StatusService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllStatus>> findAllPaged(Pageable pageable);

    @Transactional(readOnly = true)
    List<FindAllStatus> findAll();

    @Transactional(readOnly = true)
    ViewStatus findById(Long id);

    @Transactional
    ViewStatus add(AddStatus data);

    @Transactional
    ViewStatus update(UpdateStatus data);

    @Transactional
    Boolean delete(Long id);

}
