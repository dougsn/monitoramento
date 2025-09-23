package com.monitoramento.model.dvr_camera;

import com.monitoramento.model.camera.Camera;
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
@Table(name = "camera_relatorio")
public class CameraRelatorio extends RelatorioEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_camera_id", referencedColumnName = "id")
    private Camera camera;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_status_id", referencedColumnName = "id")
    private Status status;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_relatorio_id", referencedColumnName = "id")
    private Relatorio relatorio;
}
