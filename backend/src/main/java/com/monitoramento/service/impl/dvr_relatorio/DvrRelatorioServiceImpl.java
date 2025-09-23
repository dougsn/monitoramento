package com.monitoramento.service.impl.dvr_relatorio;

import com.monitoramento.controller.DvrRelatorioController;
import com.monitoramento.dto.dvr_relatorio.*;
import com.monitoramento.model.dvr_relatorio.DvrRelatorio;
import com.monitoramento.repository.dvr.DvrRepository;
import com.monitoramento.repository.dvr_relatorio.DvrRelatorioRepository;
import com.monitoramento.repository.relatorio.RelatorioRepository;
import com.monitoramento.repository.status.StatusRepository;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.dvr_relatorio.DvrRelatorioService;
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
public class DvrRelatorioServiceImpl implements DvrRelatorioService {
    private final Logger logger = Logger.getLogger(DvrRelatorioService.class.getName());

    @Autowired
    private DvrRelatorioRepository repository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private DvrRepository dvrRepository;
    @Autowired
    private RelatorioRepository relatorioRepository;
    @Autowired
    private DvrRelatorioDTOMapper mapper;
    @Autowired
    private DataDvrRelatorioDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllDvrRelatorio> assembler;


    @Override
    public PagedModel<EntityModel<AllDvrRelatorio>> findAll(Pageable pageable) {
        logger.info("Buscando todos os dvrs relatórios de forma paginada.");

        var cameras = repository.findAll(pageable);

        var dtoList = cameras.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(DvrRelatorioController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(DvrRelatorioController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public List<ViewDvrRelatorio> findAll() {
        logger.info("Buscando todos os dvrs relatórios.");
        return repository.findAll()
                .stream().map(mapper)
                .toList();
    }

    @Override
    public ViewDvrRelatorio findById(Long id) {
        logger.info("Buscando dvr relatório pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Dvr relatório de id: " + id + " não foi encontrado."));
    }

    @Override
    public ViewDvrRelatorio add(AddDvrRelatorio data) {
        logger.info("Adicionando dvr relatório");
        var dvr = dvrRepository.findById(data.getIdDvr())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + data.getIdDvr() + " não foi encontrado."));

        if (repository.existsByDiaAndDvrId(data.getDia(), data.getIdDvr()))
            throw new DataIntegratyViolationException("Já existe um relatório do dvr: " + dvr.getNome() + " do dia: " + data.getDia());

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));
        var relatorio = relatorioRepository.findById(data.getIdRelatorio())
                .orElseThrow(() -> new ObjectNotFoundException("O relatório de id: " + data.getIdRelatorio() + " não foi encontrado."));

        var dvrRelatorio = new DvrRelatorio();
        dvrRelatorio.setStatus(status);
        dvrRelatorio.setDvr(dvr);
        dvrRelatorio.setRelatorio(relatorio);
        dvrRelatorio.setDia(data.getDia());
        repository.save(dvrRelatorio);

        return mapper.apply(dvrRelatorio);
    }

    @Override
    public ViewDvrRelatorio update(UpdateDvrRelatorio data) {
        logger.info("Atualizando dvr relatório de id: " + data.getId());
        validUpdateDvrRelatorio(data.getDia(), data.getId(), data.getIdDvr());

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));
        var dvr = dvrRepository.findById(data.getIdDvr())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + data.getIdDvr() + " não foi encontrado."));
        var relatorio = relatorioRepository.findById(data.getIdRelatorio())
                .orElseThrow(() -> new ObjectNotFoundException("O relatório de id: " + data.getIdRelatorio() + " não foi encontrado."));

        var dvrRelatorioExistente = repository.findById(data.getId())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr relatório de id: " + data.getId() + " não foi encontrado para ser atualizado."));

        dvrRelatorioExistente.setStatus(status);
        dvrRelatorioExistente.setDvr(dvr);
        dvrRelatorioExistente.setRelatorio(relatorio);
        dvrRelatorioExistente.setDia(data.getDia());

        return mapper.apply(repository.save(dvrRelatorioExistente));
    }

    @Override
    public Boolean delete(Long id) {
        logger.info("Excluindo o dvr relatório de id: " + id);
        var dvrRelatorio = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Dvr relatório id: " + id + " não foi encontrada para ser deletado!"));

        repository.delete(dvrRelatorio);
        return true;
    }

    private void validUpdateDvrRelatorio(LocalDate dia, Long id, Long dvrId) {
        var relatorioByDiaAndDvr = repository.findByDiaAndDvrId(dia, dvrId);

        if (relatorioByDiaAndDvr.isPresent() && !relatorioByDiaAndDvr.get().getId().equals(id))
            throw new DataIntegratyViolationException("O relatório do dia: " + dia + " já foi cadastrado no dvr: " + relatorioByDiaAndDvr.get().getDvr().getNome() + ".");
    }

}
