package com.monitoramento.dto.camera;

import com.monitoramento.model.camera.Camera;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CameraDTOMapper implements Function<Camera, ViewCamera> {
    @Override
    public ViewCamera apply(Camera data) {
        return new ViewCamera(
                data.getId(),
                data.getNome(),
                data.getStatus().getNome(),
                data.getDvr().getNome()
        );
    }
}