package com.monitoramento.dto.relatorio;

import com.monitoramento.model.relatorio.Relatorio;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataRelatorioDTOMapper implements Function<Relatorio, AllRelatorio> {
    @Override
    public AllRelatorio apply(Relatorio data) {
        return new AllRelatorio(
                data.getId(),
                data.getDia(),
                data.getDescricao()
        );
    }
}