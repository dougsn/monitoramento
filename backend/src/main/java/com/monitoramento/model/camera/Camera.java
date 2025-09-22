package com.monitoramento.model.camera;

import com.monitoramento.model.dvr.Dvr;
import com.monitoramento.model.generic_entity.BaseEntity;
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
@Table(name = "camera")
public class Camera extends BaseEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_status_id", referencedColumnName = "id")
    private Status status;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_dvr_id", referencedColumnName = "id")
    private Dvr dvr;
}
