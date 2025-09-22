package com.monitoramento.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank(message = "O campo [email] é obrigatório.")
    @Email(message = "E-mail inválido")
    private String email;
    @NotBlank(message = "O campo [senha] é obrigatório.")
    private String password;
}