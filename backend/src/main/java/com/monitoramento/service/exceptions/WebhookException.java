package com.monitoramento.service.exceptions;

public class WebhookException extends RuntimeException{
    public WebhookException(String message) {
        super(message);
    }
}
