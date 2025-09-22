package com.monitoramento.dto.dvr;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
public class AllDvr extends RepresentationModel<AllDvr>  implements Serializable {
    private Long id;
    private String nome;
    private String nomeStatus;
}
