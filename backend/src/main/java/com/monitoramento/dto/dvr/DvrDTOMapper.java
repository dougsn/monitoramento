package com.monitoramento.dto.dvr;

import com.monitoramento.model.dvr.Dvr;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DvrDTOMapper implements Function<Dvr, ViewDvr> {
    @Override
    public ViewDvr apply(Dvr data) {
        return new ViewDvr(
                data.getId(),
                data.getNome(),
                data.getStatus().getId(),
                data.getStatus().getNome()
        );
    }
}