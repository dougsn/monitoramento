package com.monitoramento.dto.relatorio.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantidadeGeral {
    private Integer quantidade;
    private String nomeStatus;
}
