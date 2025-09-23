package com.monitoramento.dto.log.camera;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewLogStatusCamera {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;
    private String nomeCamera;
    private String statusNovo;
    private String statusAntigo;
    private String nomeNovo;
    private String nomeAntigo;
}
