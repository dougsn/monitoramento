package com.monitoramento.dto.camera;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewCamera {
    private Long id;
    private String nome;
    private String nomeStatus;
    private String nomeDvr;
}
