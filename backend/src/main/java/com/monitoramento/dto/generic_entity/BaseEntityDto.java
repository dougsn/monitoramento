package com.monitoramento.dto.generic_entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntityDto {
    private Long id;

    @NotNull(message = "O campo [nome] não pode ser vazio.")
    @Size(max = 100, message = "o campo [nome] tem que possuir no máximo 100 caracteres.")
    private String nome;

    public BaseEntityDto(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
