package com.monitoramento.model.generic_entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class LogEntity extends GenericEntity {
    @Column(name = "dia")
    private LocalDate dia;

    @Column(name = "acao")
    private String acao;

    @Column(name = "status_novo")
    private String statusNovo;

    @Column(name = "status_antigo")
    private String statusAntigo;

    @Column(name = "nome_novo")
    private String nomeNovo;

    @Column(name = "nome_antigo")
    private String nomeAntigo;

    public LogEntity(Long id, String createdBy, String updatedBy, LocalDateTime createdAt, LocalDateTime updatedAt,
                     LocalDate dia, String acao, String statusNovo, String statusAntigo, String nomeNovo, String nomeAntigo) {
        super(id, createdBy, updatedBy, createdAt, updatedAt);
        this.dia = dia;
        this.acao = acao;
        this.statusNovo = statusNovo;
        this.statusAntigo = statusAntigo;
        this.nomeNovo = nomeNovo;
        this.nomeAntigo = nomeAntigo;
    }
}
