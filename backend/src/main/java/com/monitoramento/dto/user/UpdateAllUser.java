package com.monitoramento.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateAllUser {
    @NotNull(message = "O campo [id] não pode ser vazio.")
    private Long id;
    @NotNull(message = "O campo [roleId] não pode ser vazio.")
    private Long roleId;
    @NotNull(message = "O campo [username] não pode ser vazio.")
    private String username;
    private String password;
    private String email;
}
