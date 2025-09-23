package com.monitoramento.model.log;

import com.monitoramento.model.camera.Camera;
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
@Table(name = "log_status_camera")
public class LogStatusCamera extends LogEntity {
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "fk_camera_id", referencedColumnName = "id")
    private Camera camera;
}
