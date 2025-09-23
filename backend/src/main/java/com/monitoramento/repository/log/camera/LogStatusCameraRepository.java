package com.monitoramento.repository.log.camera;


import com.monitoramento.model.log.LogStatusCamera;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LogStatusCameraRepository extends JpaRepository<LogStatusCamera, Long> {

    @Transactional(readOnly = true)
    List<LogStatusCamera> findAllByCameraId(Long cameraId);

    @Transactional(readOnly = true)
    Page<LogStatusCamera> findAllByCameraId(Long cameraId, Pageable pageable);
}