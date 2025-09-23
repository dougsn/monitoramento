package com.monitoramento.dto.camera_relatorio;

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
public class UpdateCameraRelatorio extends RelatorioEntityDto {
    @NotNull(message = "O campo [id] não pode ser vazio.")
    private Long id;
    @NotNull(message = "O campo [idStatus] não pode ser vazio.")
    private Long idStatus;
    @NotNull(message = "O campo [idCamera] não pode ser vazio.")
    private Long idCamera;
    @NotNull(message = "O campo [idRelatorio] não pode ser vazio.")
    private Long idRelatorio;
}
