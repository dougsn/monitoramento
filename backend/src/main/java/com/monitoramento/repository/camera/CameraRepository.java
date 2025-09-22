package com.monitoramento.repository.camera;


import com.monitoramento.model.camera.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {
    @Transactional(readOnly = true)
    Boolean existsByNomeAndDvrId(String nome, Long dvrId);

    @Transactional(readOnly = true)
    Optional<Camera> findByNomeAndDvrId(String nome, Long dvrId);


    @Transactional(readOnly = true)
    List<Camera> findAllByDvrId(Long dvrId);
}