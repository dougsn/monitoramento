package com.monitoramento.service.interfaces.log.camera;

import com.monitoramento.dto.log.camera.AddLogStatusCamera;
import com.monitoramento.dto.log.camera.AllLogStatusCamera;
import com.monitoramento.dto.log.camera.ViewLogStatusCamera;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface LogStatusCameraService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllLogStatusCamera>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllLogStatusCamera>> findAllByCameraId(Pageable pageable, Long cameraId);

    @Transactional(readOnly = true)
    ViewLogStatusCamera findById(Long id);

    @Transactional
    ViewLogStatusCamera add(AddLogStatusCamera data);

    @Transactional
    void deleteAllByCamera(Long cameraId);
}
