package com.monitoramento.dto.relatorio;

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
public class AllRelatorio extends RepresentationModel<AllRelatorio>  implements Serializable {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;
    private String descricao;

}
