package com.monitoramento.model.relatorio;

import com.monitoramento.model.generic_entity.RelatorioEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "relatorio")
public class Relatorio extends RelatorioEntity {
    @Column(name = "descricao")
    private String descricao;
}
