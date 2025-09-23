package com.monitoramento.dto.relatorio;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monitoramento.dto.generic_entity.RelatorioEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddRelatorio extends RelatorioEntityDto {
    private String descricao;
}
