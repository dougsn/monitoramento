package com.monitoramento.dto.log.camera;

import com.monitoramento.model.log.LogStatusCamera;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class LogStatusCameraDTOMapper implements Function<LogStatusCamera, ViewLogStatusCamera> {
    @Override
    public ViewLogStatusCamera apply(LogStatusCamera data) {
        return new ViewLogStatusCamera(
                data.getId(),
                data.getDia(),
                data.getCamera().getNome(),
                data.getStatusNovo(),
                data.getStatusAntigo(),
                data.getNomeNovo(),
                data.getNomeAntigo()
        );
    }
}