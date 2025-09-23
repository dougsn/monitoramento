package com.monitoramento.service.impl.camera_relatorio;

import com.monitoramento.controller.CameraRelatorioController;
import com.monitoramento.dto.camera_relatorio.*;
import com.monitoramento.model.dvr_camera.CameraRelatorio;
import com.monitoramento.repository.camera.CameraRepository;
import com.monitoramento.repository.camera_relatorio.CameraRelatorioRepository;
import com.monitoramento.repository.relatorio.RelatorioRepository;
import com.monitoramento.repository.status.StatusRepository;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.camera_relatorio.CameraRelatorioService;
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
public class CameraRelatorioServiceImpl implements CameraRelatorioService {
    private final Logger logger = Logger.getLogger(CameraRelatorioService.class.getName());

    @Autowired
    private CameraRelatorioRepository repository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private RelatorioRepository relatorioRepository;
    @Autowired
    private CameraRelatorioDTOMapper mapper;
    @Autowired
    private DataCameraRelatorioDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllCameraRelatorio> assembler;


    @Override
    public PagedModel<EntityModel<AllCameraRelatorio>> findAll(Pageable pageable) {
        logger.info("Buscando todas as câmeras relatórios de forma paginada.");

        var cameras = repository.findAll(pageable);

        var dtoList = cameras.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(CameraRelatorioController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(CameraRelatorioController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public List<ViewCameraRelatorio> findAll() {
        logger.info("Buscando todas as câmeras relatórios.");
        return repository.findAll()
                .stream().map(mapper)
                .toList();
    }

    @Override
    public ViewCameraRelatorio findById(Long id) {
        logger.info("Buscando câmera relatório pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Câmera relatório de id: " + id + " não foi encontrada."));
    }

    @Override
    public ViewCameraRelatorio add(AddCameraRelatorio data) {
        logger.info("Adicionando câmera relatório");
        var camera = cameraRepository.findById(data.getIdCamera())
                .orElseThrow(() -> new ObjectNotFoundException("A câmera de id: " + data.getIdCamera() + " não foi encontrada."));

        if (repository.existsByDiaAndCameraId(data.getDia(), data.getIdCamera()))
            throw new DataIntegratyViolationException("Já existe um relatório da câmera: " + camera.getNome() + " do dia: " + data.getDia());

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));
        var relatorio = relatorioRepository.findById(data.getIdRelatorio())
                .orElseThrow(() -> new ObjectNotFoundException("O relatório de id: " + data.getIdRelatorio() + " não foi encontrado."));

        var dvrRelatorio = new CameraRelatorio();
        dvrRelatorio.setStatus(status);
        dvrRelatorio.setCamera(camera);
        dvrRelatorio.setRelatorio(relatorio);
        dvrRelatorio.setDia(data.getDia());
        repository.save(dvrRelatorio);

        return mapper.apply(dvrRelatorio);
    }

    @Override
    public ViewCameraRelatorio update(UpdateCameraRelatorio data) {
        logger.info("Atualizando câmera relatório de id: " + data.getId());
        validUpdateCameraRelatorio(data.getDia(), data.getId(), data.getIdCamera());

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));
        var camera = cameraRepository.findById(data.getIdCamera())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + data.getIdCamera() + " não foi encontrado."));
        var relatorio = relatorioRepository.findById(data.getIdRelatorio())
                .orElseThrow(() -> new ObjectNotFoundException("O relatório de id: " + data.getIdRelatorio() + " não foi encontrado."));

        var dvrRelatorioExistente = repository.findById(data.getId())
                .orElseThrow(() -> new ObjectNotFoundException("O câmera relatório de id: " + data.getId() + " não foi encontrado para ser atualizado."));

        dvrRelatorioExistente.setStatus(status);
        dvrRelatorioExistente.setCamera(camera);
        dvrRelatorioExistente.setRelatorio(relatorio);
        dvrRelatorioExistente.setDia(data.getDia());

        return mapper.apply(repository.save(dvrRelatorioExistente));
    }

    @Override
    public Boolean delete(Long id) {
        logger.info("Excluindo o câmera relatório de id: " + id);
        var dvrRelatorio = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Camera relatório id: " + id + " não foi encontrada para ser deletado!"));

        repository.delete(dvrRelatorio);
        return true;
    }

    private void validUpdateCameraRelatorio(LocalDate dia, Long id, Long cameraId) {
        var relatorioByDiaAndCamera = repository.findByDiaAndCameraId(dia, cameraId);

        if (relatorioByDiaAndCamera.isPresent() && !relatorioByDiaAndCamera.get().getId().equals(id))
            throw new DataIntegratyViolationException("O relatório do dia: " + dia + " já foi cadastrado na câmera: " + relatorioByDiaAndCamera.get().getCamera().getNome() + ".");
    }

}
