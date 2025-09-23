package com.monitoramento.dto.dvr_relatorio;

import com.monitoramento.model.dvr_relatorio.DvrRelatorio;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DvrRelatorioDTOMapper implements Function<DvrRelatorio, ViewDvrRelatorio> {
    @Override
    public ViewDvrRelatorio apply(DvrRelatorio data) {
        return new ViewDvrRelatorio(
                data.getId(),
                data.getStatus().getNome(),
                data.getDvr().getNome(),
                data.getDia()
        );
    }
}