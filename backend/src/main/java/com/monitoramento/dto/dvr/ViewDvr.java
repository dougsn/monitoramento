package com.monitoramento.dto.dvr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewDvr {
    private Long id;
    private String nome;
    private String nomeStatus;
}
