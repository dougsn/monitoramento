package com.monitoramento.dto.camera;

import com.monitoramento.dto.generic_entity.BaseEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCamera extends BaseEntityDto {
    @NotNull(message = "O campo [idStatus] não pode ser vazio.")
    private Long idStatus;
    @NotNull(message = "O campo [idDvr] não pode ser vazio.")
    private Long idDvr;
}
