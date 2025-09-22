package com.monitoramento.dto.log.dvr;

import com.monitoramento.model.log.LogStatusDvr;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataLogStatusDvrDTOMapper implements Function<LogStatusDvr, AllLogStatusDvr> {
    @Override
    public AllLogStatusDvr apply(LogStatusDvr data) {
        return new AllLogStatusDvr(
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