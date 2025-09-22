package com.monitoramento.service.impl.log.dvr;

import com.monitoramento.controller.log.dvr.LogStatusDvrController;
import com.monitoramento.dto.log.dvr.*;
import com.monitoramento.model.log.LogStatusDvr;
import com.monitoramento.repository.dvr.DvrRepository;
import com.monitoramento.repository.log.dvr.LogStatusDvrRepository;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.log.dvr.LogStatusDvrService;
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
public class LogStatusDvrServiceImpl implements LogStatusDvrService {
    private final Logger logger = Logger.getLogger(LogStatusDvrService.class.getName());

    @Autowired
    private LogStatusDvrRepository repository;
    @Autowired
    private DvrRepository dvrRepository;
    @Autowired
    private LogStatusDvrDTOMapper mapper;
    @Autowired
    private DataLogStatusDvrDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllLogStatusDvr> assembler;


    @Override
    public PagedModel<EntityModel<AllLogStatusDvr>> findAll(Pageable pageable) {
        logger.info("Buscando os os logs dos dvrs");

        var dvr = repository.findAll(pageable);

        var dtoList = dvr.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(LogStatusDvrController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(LogStatusDvrController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public PagedModel<EntityModel<AllLogStatusDvr>> findAllByDvrId(Pageable pageable, Long dvrId) {
        logger.info("Buscando os os logs do dvrs de id: " + dvrId);

        var dvr = repository.findAllByDvrId(dvrId, pageable);

        var dtoList = dvr.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(LogStatusDvrController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(LogStatusDvrController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public ViewLogStatusDvr findById(Long id) {
        logger.info("Buscando log do dvr pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Logs do dvr de id: " + id + " não foram encontrados."));
    }

    @Override
    public ViewLogStatusDvr add(AddLogStatusDvr data) {
        logger.info("Adicionando log do dvr de id: " + data.getIdDvr());

        var dvr = dvrRepository.findById(data.getIdDvr())
                .orElseThrow(() -> new ObjectNotFoundException("O log do dvr de id: " + data.getIdDvr() + " não foi encontrado."));

        var log = new LogStatusDvr();
        log.setDia(data.getDia());
        log.setAcao(data.getAcao());
        log.setStatusNovo(data.getStatusNovo());
        log.setStatusAntigo(data.getStatusAntigo());
        log.setNomeNovo(data.getNomeNovo());
        log.setNomeAntigo(data.getNomeAntigo());
        log.setDvr(dvr);

        return mapper.apply(repository.save(log));
    }

    @Override
    public void deleteAllByDvr(Long dvrId) {
        logger.info("Excluindo os logs do dvr de id: " + dvrId);
        var logDvrByDvrId = repository.findAllByDvrId(dvrId);
        logDvrByDvrId.forEach(log -> repository.deleteById(log.getId()));
    }

}
