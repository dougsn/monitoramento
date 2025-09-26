package com.monitoramento.service.impl.relatorio;

import com.monitoramento.controller.RelatorioController;
import com.monitoramento.dto.camera.UpdateCamera;
import com.monitoramento.dto.camera_relatorio.AddCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.CameraRelatorioDTOMapper;
import com.monitoramento.dto.camera_relatorio.ViewCameraRelatorio;
import com.monitoramento.dto.dvr.UpdateDvr;
import com.monitoramento.dto.dvr_relatorio.AddDvrRelatorio;
import com.monitoramento.dto.dvr_relatorio.DvrRelatorioDTOMapper;
import com.monitoramento.dto.dvr_relatorio.ViewDvrRelatorio;
import com.monitoramento.dto.relatorio.*;
import com.monitoramento.dto.relatorio.create_relatorio.CameraRelatorio;
import com.monitoramento.dto.relatorio.create_relatorio.CreateRelatorio;
import com.monitoramento.dto.relatorio.create_relatorio.ListDvr;
import com.monitoramento.dto.relatorio.dashboard.QuantidadeGeral;
import com.monitoramento.model.relatorio.Relatorio;
import com.monitoramento.model.status.Status;
import com.monitoramento.repository.camera.CameraRepository;
import com.monitoramento.repository.camera_relatorio.CameraRelatorioRepository;
import com.monitoramento.repository.dvr.DvrRepository;
import com.monitoramento.repository.dvr_relatorio.DvrRelatorioRepository;
import com.monitoramento.repository.relatorio.RelatorioRepository;
import com.monitoramento.repository.status.StatusRepository;
import com.monitoramento.service.exceptions.BadRequestException;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.camera.CameraService;
import com.monitoramento.service.interfaces.camera_relatorio.CameraRelatorioService;
import com.monitoramento.service.interfaces.dvr.DvrService;
import com.monitoramento.service.interfaces.dvr_relatorio.DvrRelatorioService;
import com.monitoramento.service.interfaces.relatorio.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class RelatorioServiceImpl implements RelatorioService {
    private final Logger logger = Logger.getLogger(RelatorioService.class.getName());

    @Autowired
    private RelatorioRepository repository;
    @Autowired
    private RelatorioDTOMapper mapper;
    @Autowired
    private DataRelatorioDTOMapper dataMapper;
    @Autowired
    private DvrRepository dvrRepository;
    @Autowired
    private DvrService dvrService;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private CameraService cameraService;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private DvrRelatorioRepository dvrRelatorioRepository;
    @Autowired
    private DvrRelatorioService dvrRelatorioService;
    @Autowired
    private DvrRelatorioDTOMapper dvrRelatorioDTOMapper;
    @Autowired
    private CameraRelatorioRepository cameraRelatorioRepository;
    @Autowired
    private CameraRelatorioService cameraRelatorioService;
    @Autowired
    private CameraRelatorioDTOMapper cameraRelatorioDTOMapper;
    @Autowired
    PagedResourcesAssembler<AllRelatorio> assembler;


    @Override
    public PagedModel<EntityModel<AllRelatorio>> findAll(Pageable pageable) {
        logger.info("Buscando os relatório.");

        var relatorio = repository.findAll(pageable);

        var dtoList = relatorio.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(RelatorioController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(RelatorioController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public ViewRelatorio findById(Long id) {
        logger.info("Buscando relatório pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Relatório de id: " + id + " não foi encontrado."));
    }

    @Override
    public void createRelatorio(CreateRelatorio data) {
        logger.info("Criando novo relatório com os demais fluxos de atualização dos status diários.");

        var relatorio = new AddRelatorio();
        relatorio.setDia(data.getDia());
        relatorio.setDescricao(data.getDescricao());
        var newRelatorio = add(relatorio);

        for (ListDvr dvr : data.getDvrs()) {
            var statusDvr = statusRepository.findById(dvr.getStatusIdDvr())
                    .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + dvr.getStatusIdDvr() + " não foi encontrada."));
            var dvrEntity =
                    dvrRepository.findById(dvr.getDvrId())
                            .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + dvr.getDvrId() + " não foi encontrado."));

            logger.info("Criando o DVR Relatório.");
            var dvrRelatorio = new AddDvrRelatorio();
            dvrRelatorio.setDia(data.getDia());
            dvrRelatorio.setIdRelatorio(newRelatorio.getId());
            dvrRelatorio.setIdDvr(dvrEntity.getId());
            dvrRelatorio.setIdStatus(statusDvr.getId());
            dvrRelatorioService.add(dvrRelatorio);

            logger.info("Atualizando o status do DVR e criando seus log's.");
            var updateDvr = new UpdateDvr();
            updateDvr.setId(dvrEntity.getId());
            updateDvr.setNome(dvrEntity.getNome());
            updateDvr.setDia(data.getDia());
            updateDvr.setIdStatus(statusDvr.getId());
            dvrService.update(updateDvr);

            if (dvr.getCameras() == null)
                throw new BadRequestException("Há dvrs que não possuem câmeras vinculadas, faça o vinculo para criar o relatório!");

            for (CameraRelatorio camera : dvr.getCameras()) {
                var statusCamera = statusRepository.findById(camera.getStatusId())
                        .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + camera.getStatusId() + " não foi encontrada."));
                var cameraEntity = cameraRepository.findById(camera.getCameraId())
                        .orElseThrow(() -> new ObjectNotFoundException("A câmera de id: " + camera.getCameraId() + " não foi encontrada."));

                logger.info("Atualizando o status da câmera e criando seus log's.");
                var updateCamera = new UpdateCamera();
                updateCamera.setId(cameraEntity.getId());
                updateCamera.setDia(data.getDia());
                updateCamera.setNome(cameraEntity.getNome());
                updateCamera.setIdDvr(dvrEntity.getId());
                updateCamera.setIdStatus(statusCamera.getId());
                cameraService.update(updateCamera);

                logger.info("Criando o Câmera Relatório.");
                var cameraRelatorio = new AddCameraRelatorio();
                cameraRelatorio.setDia(data.getDia());
                cameraRelatorio.setIdCamera(cameraEntity.getId());
                cameraRelatorio.setIdRelatorio(newRelatorio.getId());
                cameraRelatorio.setIdStatus(statusCamera.getId());
                cameraRelatorioService.add(cameraRelatorio);
            }
        }
    }

    @Override
    public void viewRelatorio(LocalDate dia) {
        var status = statusRepository.findAll();
        var quantidadeGeralDvr = new ArrayList<QuantidadeGeral>();
        var quantidadeGeralCamera = new ArrayList<QuantidadeGeral>();

        // Criar lista similar a doc do useChart do chakraui v3

        for (Status s : status) {
            var dvrRelatorio = dvrRelatorioRepository
                    .findAllByStatusIdAndDia(s.getId(), dia).stream().map(dvrRelatorioDTOMapper).toList();
            int totalDvr = dvrRelatorio.size();
            quantidadeGeralDvr.add(new QuantidadeGeral(totalDvr, s.getNome()));


            for (ViewDvrRelatorio dvr : dvrRelatorio) {

            }


            var cameraRelatorio = cameraRelatorioRepository
                    .findAllByStatusIdAndDia(s.getId(), dia).stream().map(cameraRelatorioDTOMapper).toList();
            var totalCamera = cameraRelatorio.size();
            quantidadeGeralCamera.add(new QuantidadeGeral(totalCamera, s.getNome()));

            for (ViewCameraRelatorio camera : cameraRelatorio) {

            }

        }

    }

    @Override
    public ViewRelatorio add(AddRelatorio data) {
        logger.info("Adicionando relatorio");
        if (repository.existsByDia(data.getDia()))
            throw new DataIntegratyViolationException("O relatório do dia: " + data.getDia() + " já foi criado.");

        var relatorio = new Relatorio();
        relatorio.setDescricao(data.getDescricao());
        relatorio.setDia(data.getDia());

        return mapper.apply(repository.save(relatorio));
    }

    @Override
    public ViewRelatorio update(UpdateRelatorio data) {
        logger.info("Atualizando relatório de id: " + data.getId());
        validUpdateRelatorio(data.getDia(), data.getId());

        var relatorioExistente = repository.findById(data.getId())
                .orElseThrow(() -> new ObjectNotFoundException("O relatório de id: " + data.getId() + " não foi encontrado para ser atualizado."));

        relatorioExistente.setDescricao(data.getDescricao());
        relatorioExistente.setDia(data.getDia());

        return mapper.apply(repository.save(relatorioExistente));
    }

    @Override
    public Boolean delete(Long id) {
        logger.info("Excluindo o relatório de id: " + id);
        var relatorio = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Relatório id: " + id + " não foi encontrado para ser deletado!"));

        if (!cameraRelatorioRepository.findAllByRelatorioId(id).isEmpty())
            throw new DataIntegratyViolationException("O relatório não pode ser deletado, pois existem relatórios de câmeras vinculadas!");

        if (!dvrRelatorioRepository.findAllByRelatorioId(id).isEmpty())
            throw new DataIntegratyViolationException("O relatório não pode ser deletado, pois existem relatórios de dvrs vinculados!");

        repository.delete(relatorio);
        return true;
    }

    private void validUpdateRelatorio(LocalDate dia, Long id) {
        var relatorioByDia = repository.findByDia(dia);

        if (relatorioByDia.isPresent() && !relatorioByDia.get().getId().equals(id))
            throw new DataIntegratyViolationException("O relatório do dia: " + dia + " já foi cadastrado.");
    }

}
