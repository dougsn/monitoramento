package com.monitoramento.dto.log.camera;

import com.monitoramento.dto.generic_entity.LogEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddLogStatusCamera extends LogEntityDto {
    @NotNull(message = "O campo [idCamera] não pode ser vazio.")
    private Long idCamera;
}
