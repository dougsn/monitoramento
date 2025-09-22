package com.monitoramento.dto.camera;

import com.monitoramento.model.camera.Camera;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataCameraDTOMapper implements Function<Camera, AllCamera> {
    @Override
    public AllCamera apply(Camera data) {
        return new AllCamera(
                data.getId(),
                data.getNome(),
                data.getStatus().getNome(),
                data.getDvr().getNome()
        );
    }
}