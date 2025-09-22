package com.monitoramento.security.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SignupRequest {
    @NotBlank(message = "O campo [username] é obrigatório.")
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank(message = "O campo [email] é obrigatório.")
    @Size(max = 50)
    @Email
    private String email;

    @NotNull(message = "O campo [roleId] é obrigatório.")
    @Setter
    @Getter
    private Long roleId;

    @NotBlank(message = "O campo [senha] é obrigatório.")
    @Size(min = 6, max = 40)
    private String password;
}