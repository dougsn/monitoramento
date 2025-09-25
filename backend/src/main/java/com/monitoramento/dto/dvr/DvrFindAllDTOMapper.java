package com.monitoramento.dto.dvr;

import com.monitoramento.model.dvr.Dvr;
import com.monitoramento.model.status.Status;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DvrFindAllDTOMapper implements Function<Dvr, FindAllDvr> {
    @Override
    public FindAllDvr apply(Dvr data) {
        return new FindAllDvr(
                data.getId(),
                data.getNome()
        );
    }
}