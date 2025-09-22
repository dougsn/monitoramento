package com.monitoramento.dto.log.dvr;

import com.monitoramento.dto.generic_entity.LogEntityDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateLogStatusDvr extends LogEntityDto {
    @NotNull(message = "O campo [id] não pode ser vazio.")
    private Long id;
    @NotNull(message = "O campo [idDvr] não pode ser vazio.")
    private Long idDvr;
}
