package com.monitoramento.dto.generic_entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class LogEntityDto {
    private Long id;

    @NotNull(message = "O campo [dia] não pode ser vazio.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dia;

    @NotNull(message = "O campo [acao] não pode ser vazio.")
    @Size(max = 10, message = "o campo [acao] tem que possuir no máximo 10 caracteres.")
    private String acao;

    @Size(max = 100, message = "o campo [statusNovo] tem que possuir no máximo 100 caracteres.")
    private String statusNovo;

    @Size(max = 100, message = "o campo [statusAntigo] tem que possuir no máximo 100 caracteres.")
    private String statusAntigo;

    @Size(max = 100, message = "o campo [nomeNovo] tem que possuir no máximo 100 caracteres.")
    private String nomeNovo;

    @Size(max = 100, message = "o campo [nomeAntigo] tem que possuir no máximo 100 caracteres.")
    private String nomeAntigo;
}
