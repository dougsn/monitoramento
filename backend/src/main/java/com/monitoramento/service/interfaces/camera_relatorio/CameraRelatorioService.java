package com.monitoramento.service.interfaces.camera_relatorio;

import com.monitoramento.dto.camera_relatorio.AddCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.AllCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.UpdateCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.ViewCameraRelatorio;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CameraRelatorioService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllCameraRelatorio>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    List<ViewCameraRelatorio> findAll();

    @Transactional(readOnly = true)
    ViewCameraRelatorio findById(Long id);

    @Transactional
    ViewCameraRelatorio add(AddCameraRelatorio data);

    @Transactional
    ViewCameraRelatorio update(UpdateCameraRelatorio data);

    @Transactional
    Boolean delete(Long id);

}
