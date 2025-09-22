package com.monitoramento.dto.log.dvr;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.LocalDate;


@AllArgsConstructor
@Getter
@Setter
public class AllLogStatusDvr extends RepresentationModel<AllLogStatusDvr> implements Serializable {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;
    private String nomeDvr;
    private String statusNovo;
    private String statusAntigo;
    private String nomeNovo;
    private String nomeAntigo;
}
