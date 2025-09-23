package com.monitoramento.dto.camera_relatorio;

import com.monitoramento.model.dvr_camera.CameraRelatorio;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CameraRelatorioDTOMapper implements Function<CameraRelatorio, ViewCameraRelatorio> {
    @Override
    public ViewCameraRelatorio apply(CameraRelatorio data) {
        return new ViewCameraRelatorio(
                data.getId(),
                data.getStatus().getNome(),
                data.getCamera().getNome(),
                data.getDia()
        );
    }
}