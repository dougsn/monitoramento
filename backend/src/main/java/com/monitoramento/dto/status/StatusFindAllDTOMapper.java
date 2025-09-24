package com.monitoramento.dto.status;

import com.monitoramento.model.status.Status;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StatusFindAllDTOMapper implements Function<Status, FindAllStatus> {
    @Override
    public FindAllStatus apply(Status data) {
        return new FindAllStatus(
                data.getId(),
                data.getNome()
        );
    }
}