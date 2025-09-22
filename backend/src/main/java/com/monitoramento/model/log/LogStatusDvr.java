package com.monitoramento.model.log;

import com.monitoramento.model.dvr.Dvr;
import com.monitoramento.model.generic_entity.LogEntity;
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
@Table(name = "log_status_dvr")
public class LogStatusDvr extends LogEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_dvr_id", referencedColumnName = "id")
    private Dvr dvr;
}
