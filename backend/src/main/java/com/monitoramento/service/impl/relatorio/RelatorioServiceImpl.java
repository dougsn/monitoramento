package com.monitoramento.service.impl.relatorio;

import com.monitoramento.controller.RelatorioController;
import com.monitoramento.dto.relatorio.*;
import com.monitoramento.model.relatorio.Relatorio;
import com.monitoramento.repository.camera_relatorio.CameraRelatorioRepository;
import com.monitoramento.repository.dvr_relatorio.DvrRelatorioRepository;
import com.monitoramento.repository.relatorio.RelatorioRepository;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.relatorio.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private DvrRelatorioRepository dvrRelatorioRepository;
    @Autowired
    private CameraRelatorioRepository cameraRelatorioRepository;
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
