package com.monitoramento.service.exceptions;

public class EmptyAttributeException extends RuntimeException{
    public EmptyAttributeException(String message) {
        super(message);
    }
}
