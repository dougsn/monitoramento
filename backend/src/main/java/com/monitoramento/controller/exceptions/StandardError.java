package com.monitoramento.controller.exceptions;


import java.time.LocalDateTime;

public class StandardError {
    private LocalDateTime timestamp;
    private Integer status;
    private String errorMessage;
    private String path;

    public StandardError() {}

    public StandardError(LocalDateTime timestamp, Integer status, String errorMessage, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.errorMessage = errorMessage;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
