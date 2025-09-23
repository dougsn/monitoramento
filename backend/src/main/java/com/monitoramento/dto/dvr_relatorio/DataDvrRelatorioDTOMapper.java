package com.monitoramento.dto.dvr_relatorio;

import com.monitoramento.model.dvr_relatorio.DvrRelatorio;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataDvrRelatorioDTOMapper implements Function<DvrRelatorio, AllDvrRelatorio> {
    @Override
    public AllDvrRelatorio apply(DvrRelatorio data) {
        return new AllDvrRelatorio(
                data.getId(),
                data.getStatus().getNome(),
                data.getDvr().getNome(),
                data.getDia()
        );
    }
}