package com.monitoramento.dto.dvr;

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
public class UpdateDvr extends BaseEntityDto {
    @NotNull(message = "O campo [id] não pode ser vazio.")
    private Long id;
    @NotNull(message = "O campo [idStatus] não pode ser vazio.")
    private Long idStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;
}
