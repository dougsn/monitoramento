package com.monitoramento.service.impl.camera;

import com.monitoramento.controller.CameraController;
import com.monitoramento.dto.camera.*;
import com.monitoramento.dto.log.camera.AddLogStatusCamera;
import com.monitoramento.model.camera.Camera;
import com.monitoramento.repository.camera.CameraRepository;
import com.monitoramento.repository.camera_relatorio.CameraRelatorioRepository;
import com.monitoramento.repository.dvr.DvrRepository;
import com.monitoramento.repository.status.StatusRepository;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.camera.CameraService;
import com.monitoramento.service.interfaces.log.camera.LogStatusCameraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CameraServiceImpl implements CameraService {
    private final Logger logger = Logger.getLogger(CameraService.class.getName());

    @Autowired
    private CameraRepository repository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CameraRelatorioRepository cameraRelatorioRepository;
    @Autowired
    private DvrRepository dvrRepository;
    @Autowired
    private LogStatusCameraService logCamera;
    @Autowired
    private CameraDTOMapper mapper;
    @Autowired
    private DataCameraDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllCamera> assembler;


    @Override
    public PagedModel<EntityModel<AllCamera>> findAll(Pageable pageable) {
        logger.info("Buscando todas as câmeras de forma paginada.");

        var cameras = repository.findAll(pageable);

        var dtoList = cameras.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(CameraController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(CameraController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public List<ViewCamera> findAll() {
        logger.info("Buscando todas as câmeras.");
        return repository.findAll()
                .stream().map(mapper)
                .toList();
    }

    @Override
    public ViewCamera findById(Long id) {
        logger.info("Buscando camera pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Camera de id: " + id + " não foi encontrado."));
    }

    @Override
    public ViewCamera add(AddCamera data) {
        logger.info("Adicionando câmera");
        var dvr = dvrRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + data.getIdDvr() + " não foi encontrado."));

        if (repository.existsByNomeAndDvrId(data.getNome(), data.getIdDvr()))
            throw new DataIntegratyViolationException("A câmera de nome: " + data.getNome() + " já existe no dvr: " + dvr.getNome() + ".");

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));

        var camera = new Camera();
        camera.setNome(data.getNome());
        camera.setStatus(status);
        camera.setDvr(dvr);
        repository.save(camera);

        var log = new AddLogStatusCamera();
        log.setDia(LocalDate.now());
        log.setAcao("CREATE");
        log.setStatusNovo(camera.getStatus().getNome());
        log.setStatusAntigo("");
        log.setNomeNovo(camera.getNome());
        log.setNomeAntigo("");
        log.setIdCamera(camera.getId());
        logCamera.add(log);

        return mapper.apply(camera);
    }

    @Override
    public ViewCamera update(UpdateCamera data) {
        logger.info("Atualizando câmera de id: " + data.getId());
        validUpdateCamera(data.getNome(), data.getId(), data.getIdDvr());

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));
        var dvr = dvrRepository.findById(data.getIdDvr())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + data.getIdDvr() + " não foi encontrado."));
        var cameraExistente = repository.findById(data.getId())
                .orElseThrow(() -> new ObjectNotFoundException("A câmera de id: " + data.getId() + " não foi encontrado para ser atualizado."));

        var log = new AddLogStatusCamera();
        log.setDia(data.getDia() == null ? LocalDate.now() : data.getDia());
        log.setAcao("UPDATE");
        log.setStatusNovo(status.getNome());
        log.setStatusAntigo(cameraExistente.getStatus().getNome());
        log.setNomeNovo(data.getNome());
        log.setNomeAntigo(cameraExistente.getNome());
        log.setIdCamera(cameraExistente.getId());
        logCamera.add(log);


        cameraExistente.setNome(data.getNome());
        cameraExistente.setStatus(status);
        cameraExistente.setStatus(status);
        cameraExistente.setDvr(dvr);

        return mapper.apply(repository.save(cameraExistente));
    }

    @Override
    public Boolean delete(Long id) {
        logger.info("Excluindo o câmera de id: " + id);
        var camera = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Câmera id: " + id + " não foi encontrada para ser deletado!"));

        if (!cameraRelatorioRepository.findAllByCameraId(id).isEmpty())
            throw new DataIntegratyViolationException("A câmera não pode ser deletada, pois existem relatórios de câmeras vinculadas!");

        logCamera.deleteAllByCamera(id);
        repository.delete(camera);
        return true;
    }

    private void validUpdateCamera(String nome, Long id, Long dvrId) {
        var cameraByNomeAndDvr = repository.findByNomeAndDvrId(nome, dvrId);

        if (cameraByNomeAndDvr.isPresent() && !cameraByNomeAndDvr.get().getId().equals(id))
            throw new DataIntegratyViolationException("A câmera de nome: " + nome + " já foi cadastrado no dvr: " + cameraByNomeAndDvr.get().getDvr().getNome() + ".");
    }

}
