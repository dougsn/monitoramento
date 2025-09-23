package com.monitoramento.dto.dvr_relatorio;

import com.monitoramento.dto.generic_entity.BaseEntityDto;
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
public class AddDvrRelatorio extends RelatorioEntityDto {
    @NotNull(message = "O campo [idStatus] não pode ser vazio.")
    private Long idStatus;
    @NotNull(message = "O campo [idDvr] não pode ser vazio.")
    private Long idDvr;
    @NotNull(message = "O campo [idRelatorio] não pode ser vazio.")
    private Long idRelatorio;
}
