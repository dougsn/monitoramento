package com.monitoramento.dto.camera_relatorio;

import com.monitoramento.model.dvr_camera.CameraRelatorio;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataCameraRelatorioDTOMapper implements Function<CameraRelatorio, AllCameraRelatorio> {
    @Override
    public AllCameraRelatorio apply(CameraRelatorio data) {
        return new AllCameraRelatorio(
                data.getId(),
                data.getStatus().getNome(),
                data.getCamera().getNome(),
                data.getDia()
        );
    }
}