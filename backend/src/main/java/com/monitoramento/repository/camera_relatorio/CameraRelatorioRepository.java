package com.monitoramento.repository.camera_relatorio;


import com.monitoramento.model.dvr_camera.CameraRelatorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRelatorioRepository extends JpaRepository<CameraRelatorio, Long> {
    @Transactional(readOnly = true)
    Boolean existsByDia(LocalDate dia);

    @Transactional(readOnly = true)
    Boolean existsByDiaAndCameraId(LocalDate dia, Long dvrId);

    @Transactional(readOnly = true)
    Optional<CameraRelatorio> findByDiaAndCameraId(LocalDate dia, Long dvrId);

    @Transactional(readOnly = true)
    List<CameraRelatorio> findAllByDia(LocalDate dia);
}