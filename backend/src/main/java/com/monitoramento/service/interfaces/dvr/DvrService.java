package com.monitoramento.service.interfaces.dvr;

import com.monitoramento.dto.camera.ViewCamera;
import com.monitoramento.dto.dvr.*;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DvrService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllDvr>> findAll(Pageable pageable);
    @Transactional(readOnly = true)
    List<FindAllDvr> findAll();

    @Transactional(readOnly = true)
    ViewDvr findById(Long id);

    @Transactional
    ViewDvr add(AddDvr data);

    @Transactional
    ViewDvr update(UpdateDvr data);

    @Transactional
    Boolean delete(Long id);

}
