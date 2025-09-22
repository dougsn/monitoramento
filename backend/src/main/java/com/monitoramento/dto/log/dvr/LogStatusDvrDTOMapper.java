package com.monitoramento.dto.log.dvr;

import com.monitoramento.model.log.LogStatusDvr;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class LogStatusDvrDTOMapper implements Function<LogStatusDvr, ViewLogStatusDvr> {
    @Override
    public ViewLogStatusDvr apply(LogStatusDvr data) {
        return new ViewLogStatusDvr(
                data.getId(),
                data.getDia(),
                data.getDvr().getNome(),
                data.getStatusNovo(),
                data.getStatusAntigo(),
                data.getNomeNovo(),
                data.getNomeAntigo()
        );
    }
}