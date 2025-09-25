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
public class CreateRelatorio extends RelatorioEntityDto {
    private String descricao;
    @NotNull(message = "A lista de [dvrs] não pode ser vazia.")
    private List<ListDvr> dvrs;
}
