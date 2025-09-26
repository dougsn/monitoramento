package com.monitoramento.dto.dvr;

import com.monitoramento.dto.camera.DataCameraDTOMapper;
import com.monitoramento.model.dvr.Dvr;
import com.monitoramento.repository.camera.CameraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DvrFindAllDTOMapper implements Function<Dvr, FindAllDvr> {
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private DataCameraDTOMapper dataCameraDTOMapper;

    @Override
    public FindAllDvr apply(Dvr data) {
        var cameras = cameraRepository
                .findAllByDvrId(data.getId()).stream().map(dataCameraDTOMapper).collect(Collectors.toList());

        return new FindAllDvr(
                data.getId(),
                data.getNome(),
                cameras
        );
    }
}