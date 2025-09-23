package com.monitoramento.dto.log.camera;

import com.monitoramento.model.log.LogStatusCamera;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataLogStatusCameraDTOMapper implements Function<LogStatusCamera, AllLogStatusCamera> {
    @Override
    public AllLogStatusCamera apply(LogStatusCamera data) {
        return new AllLogStatusCamera(
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