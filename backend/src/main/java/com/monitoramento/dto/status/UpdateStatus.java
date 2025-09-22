package com.monitoramento.dto.status;

import com.monitoramento.dto.generic_entity.BaseEntityDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateStatus extends BaseEntityDto {
    @NotNull(message = "O campo [id] não pode ser vazio.")
    private Long id;
    @NotNull(message = "O campo [cor] não pode ser vazio.")
    @Size(max = 100, message = "o campo [cor] tem que possuir no máximo 100 caracteres.")
    private String cor;
}
