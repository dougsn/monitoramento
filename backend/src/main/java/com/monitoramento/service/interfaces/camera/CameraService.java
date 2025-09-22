package com.monitoramento.service.interfaces.camera;

import com.monitoramento.dto.camera.AddCamera;
import com.monitoramento.dto.camera.AllCamera;
import com.monitoramento.dto.camera.UpdateCamera;
import com.monitoramento.dto.camera.ViewCamera;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CameraService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllCamera>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    List<ViewCamera> findAll();

    @Transactional(readOnly = true)
    ViewCamera findById(Long id);

    @Transactional
    ViewCamera add(AddCamera data);

    @Transactional
    ViewCamera update(UpdateCamera data);

    @Transactional
    Boolean delete(Long id);

}
