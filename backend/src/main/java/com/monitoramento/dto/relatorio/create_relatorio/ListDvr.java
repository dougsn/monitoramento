package com.monitoramento.dto.relatorio.create_relatorio;

import com.monitoramento.dto.generic_entity.RelatorioEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListDvr extends RelatorioEntityDto {
    @NotNull(message = "O campo [dvrId] não pode ser vazio.")
    private Long dvrId;
    @NotNull(message = "O campo [statusIdDvr] não pode ser vazio.")
    private Long statusIdDvr;
    @NotNull(message = "A lista de [cameras] não pode ser vazia.")
    private List<CameraRelatorio> cameras;
}
