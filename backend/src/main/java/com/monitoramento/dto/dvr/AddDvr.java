package com.monitoramento.dto.dvr;

import com.monitoramento.dto.generic_entity.BaseEntityDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDvr extends BaseEntityDto {
    @NotNull(message = "O campo [idStatus] não pode ser vazio.")
    private Long idStatus;
}
