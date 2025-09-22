package com.monitoramento.service.impl.status;

import com.monitoramento.controller.StatusController;
import com.monitoramento.dto.status.*;
import com.monitoramento.model.status.Status;
import com.monitoramento.repository.status.StatusRepository;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.status.StatusService;
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
public class StatusServiceImpl implements StatusService {
    private final Logger logger = Logger.getLogger(StatusService.class.getName());

    @Autowired
    private StatusRepository repository;
    @Autowired
    private StatusDTOMapper mapper;

    @Autowired
    private DataStatusDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllStatus> assembler;


    @Override
    public PagedModel<EntityModel<AllStatus>> findAll(Pageable pageable) {
        logger.info("Buscando os status.");

        var status = repository.findAll(pageable);

        var dtoList = status.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(StatusController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(StatusController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public ViewStatus findById(Long id) {
        logger.info("Buscando status pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("status de id: " + id + " não foi encontrado."));
    }

    @Override
    public ViewStatus add(AddStatus data) {
        logger.info("Adicionando status");
        if (repository.existsByCorOrNome(data.getCor(), data.getNome()))
            throw new DataIntegratyViolationException("O status de nome ou cor enviado já existe.");

        var status = new Status();
        status.setNome(data.getNome());
        status.setCor(data.getCor());

        return mapper.apply(repository.save(status));
    }

    @Override
    public ViewStatus update(UpdateStatus data) {
        logger.info("Atualizando status de id: " + data.getId());
        validUpdateStatus(data.getNome(), data.getCor(), data.getId());

        var statusExistente = repository.findById(data.getId())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getId() + " não foi encontrado para ser atualizado."));

        statusExistente.setNome(data.getNome());
        statusExistente.setCor(data.getCor());

        return mapper.apply(repository.save(statusExistente));
    }

    @Override
    public Boolean delete(Long id) {
        logger.info("Excluindo o status de id: " + id);
        var status = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Status id: " + id + " não foi encontrada para ser deletado!"));
        repository.delete(status);
        return true;
    }

    private void validUpdateStatus(String nome, String cor, Long id) {
        var statusByNome = repository.findByNome(nome);
        var statusByCor = repository.findByCor(cor);

        if (statusByNome.isPresent() && !statusByNome.get().getId().equals(id))
            throw new DataIntegratyViolationException("O nome: " + nome + " já foi cadastrado.");

        if (statusByCor.isPresent() && !statusByCor.get().getId().equals(id))
            throw new DataIntegratyViolationException("A cor: " + cor + " já foi cadastrada.");
    }

}
