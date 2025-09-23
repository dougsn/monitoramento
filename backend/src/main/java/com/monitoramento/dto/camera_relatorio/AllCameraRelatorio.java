package com.monitoramento.dto.camera_relatorio;

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
public class AllCameraRelatorio extends RepresentationModel<AllCameraRelatorio> implements Serializable {
    private Long id;
    private String nomeStatus;
    private String nomeCamera;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;
}
