package com.monitoramento.dto.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
public class AllStatus extends RepresentationModel<AllStatus>  implements Serializable {
    private Long id;
    private String nome;
    private String cor;
}
