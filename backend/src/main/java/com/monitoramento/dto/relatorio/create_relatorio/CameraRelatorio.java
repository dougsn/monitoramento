package com.monitoramento.dto.relatorio.create_relatorio;

import com.monitoramento.dto.generic_entity.RelatorioEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CameraRelatorio extends RelatorioEntityDto {
    @NotNull(message = "O campo [cameraId] não pode ser vazio.")
    private Long cameraId;
    @NotNull(message = "O campo [statusId] não pode ser vazio.")
    private Long statusId;
}
