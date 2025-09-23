package com.monitoramento.service.impl.log.camera;

import com.monitoramento.controller.log.camera.LogStatusCameraController;
import com.monitoramento.dto.log.camera.*;
import com.monitoramento.model.log.LogStatusCamera;
import com.monitoramento.repository.camera.CameraRepository;
import com.monitoramento.repository.log.camera.LogStatusCameraRepository;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.log.camera.LogStatusCameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LogStatusCamerarServiceImpl implements LogStatusCameraService {
    private final Logger logger = Logger.getLogger(LogStatusCameraService.class.getName());

    @Autowired
    private LogStatusCameraRepository repository;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private LogStatusCameraDTOMapper mapper;
    @Autowired
    private DataLogStatusCameraDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllLogStatusCamera> assembler;


    @Override
    public PagedModel<EntityModel<AllLogStatusCamera>> findAll(Pageable pageable) {
        logger.info("Buscando os logs das câmeras");

        var camera = repository.findAll(pageable);

        var dtoList = camera.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(LogStatusCameraController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(LogStatusCameraController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public PagedModel<EntityModel<AllLogStatusCamera>> findAllByCameraId(Pageable pageable, Long cameraId) {
        logger.info("Buscando os os logs da câmeras de id: " + cameraId);

        var camera = repository.findAllByCameraId(cameraId, pageable);

        var dtoList = camera.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(LogStatusCameraController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(LogStatusCameraController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public ViewLogStatusCamera findById(Long id) {
        logger.info("Buscando log da câmera pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Logs da câmera de id: " + id + " não foram encontrados."));
    }

    @Override
    public ViewLogStatusCamera add(AddLogStatusCamera data) {
        logger.info("Adicionando log da câmera de id: " + data.getIdCamera());

        var camera = cameraRepository.findById(data.getIdCamera())
                .orElseThrow(() -> new ObjectNotFoundException("O log da câmera de id: " + data.getIdCamera() + " não foi encontrado."));

        var log = new LogStatusCamera();
        log.setDia(data.getDia());
        log.setAcao(data.getAcao());
        log.setStatusNovo(data.getStatusNovo());
        log.setStatusAntigo(data.getStatusAntigo());
        log.setNomeNovo(data.getNomeNovo());
        log.setNomeAntigo(data.getNomeAntigo());
        log.setCamera(camera);

        return mapper.apply(repository.save(log));
    }

    @Override
    public void deleteAllByCamera(Long cameraId) {
        logger.info("Excluindo os logs da câmera de id: " + cameraId);
        var logCameraByCameraId = repository.findAllByCameraId(cameraId);
        logCameraByCameraId.forEach(log -> repository.deleteById(log.getId()));
    }

}
