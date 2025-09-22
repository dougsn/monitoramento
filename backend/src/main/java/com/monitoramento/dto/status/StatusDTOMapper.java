package com.monitoramento.dto.status;

import com.monitoramento.model.status.Status;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class StatusDTOMapper implements Function<Status, ViewStatus> {
    @Override
    public ViewStatus apply(Status data) {
        return new ViewStatus(
                data.getId(),
                data.getNome(),
                data.getCor()
        );
    }
}