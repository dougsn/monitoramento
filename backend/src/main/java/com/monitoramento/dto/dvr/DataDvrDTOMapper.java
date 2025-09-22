package com.monitoramento.dto.dvr;

import com.monitoramento.model.dvr.Dvr;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataDvrDTOMapper implements Function<Dvr, AllDvr> {
    @Override
    public AllDvr apply(Dvr data) {
        return new AllDvr(
                data.getId(),
                data.getNome(),
                data.getStatus().getNome()
        );
    }
}