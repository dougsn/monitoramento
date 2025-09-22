package com.monitoramento.security.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageResponse {
    private String errorMessage;

    public MessageResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}