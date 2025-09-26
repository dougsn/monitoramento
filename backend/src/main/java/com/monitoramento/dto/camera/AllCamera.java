package com.monitoramento.dto.camera;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;


@AllArgsConstructor
@Getter
@Setter
public class AllCamera extends RepresentationModel<AllCamera> implements Serializable {
    private Long id;
    private String nome;
    private Long statusId;
    private String nomeStatus;
    private String nomeDvr;
}
