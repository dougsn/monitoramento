package com.monitoramento.dto.relatorio;

import com.monitoramento.model.relatorio.Relatorio;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RelatorioDTOMapper implements Function<Relatorio, ViewRelatorio> {
    @Override
    public ViewRelatorio apply(Relatorio data) {
        return new ViewRelatorio(
                data.getId(),
                data.getDia(),
                data.getDescricao()
        );
    }
}