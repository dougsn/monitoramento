package com.monitoramento.dto.status;

import com.monitoramento.model.status.Status;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class DataStatusDTOMapper implements Function<Status, AllStatus> {
    @Override
    public AllStatus apply(Status data) {
        return new AllStatus(
                data.getId(),
                data.getNome(),
                data.getCor()
        );
    }
}