package com.monitoramento.model.generic_entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends GenericEntity {

    @Column(name = "nome")
    private String nome;

    public BaseEntity(Long id, String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt, String nome) {
        super(id, createdBy, updatedBy, createdAt, updatedAt);
        this.nome = nome;
    }
}
