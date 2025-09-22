package com.monitoramento.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserResponse {
    private Long id;
    private String username;
}
