package com.monitoramento.dto.camera;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.monitoramento.dto.generic_entity.BaseEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCamera extends BaseEntityDto {
    @NotNull(message = "O campo [id] não pode ser vazio.")
    private Long id;
    @NotNull(message = "O campo [idStatus] não pode ser vazio.")
    private Long idStatus;
    @NotNull(message = "O campo [idDvr] não pode ser vazio.")
    private Long idDvr;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;
}
