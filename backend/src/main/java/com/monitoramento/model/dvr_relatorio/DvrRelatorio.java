package com.monitoramento.model.dvr_relatorio;

import com.monitoramento.model.dvr.Dvr;
import com.monitoramento.model.generic_entity.RelatorioEntity;
import com.monitoramento.model.relatorio.Relatorio;
import com.monitoramento.model.status.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dvr_relatorio")
public class DvrRelatorio extends RelatorioEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_dvr_id", referencedColumnName = "id")
    private Dvr dvr;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_status_id", referencedColumnName = "id")
    private Status status;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_relatorio_id", referencedColumnName = "id")
    private Relatorio relatorio;
}
