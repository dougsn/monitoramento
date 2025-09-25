package com.monitoramento.service.impl.dvr;

import com.monitoramento.controller.DvrController;
import com.monitoramento.dto.dvr.*;
import com.monitoramento.dto.log.dvr.AddLogStatusDvr;
import com.monitoramento.model.dvr.Dvr;
import com.monitoramento.repository.camera.CameraRepository;
import com.monitoramento.repository.dvr.DvrRepository;
import com.monitoramento.repository.dvr_relatorio.DvrRelatorioRepository;
import com.monitoramento.repository.status.StatusRepository;
import com.monitoramento.service.exceptions.DataIntegratyViolationException;
import com.monitoramento.service.exceptions.ObjectNotFoundException;
import com.monitoramento.service.interfaces.dvr.DvrService;
import com.monitoramento.service.interfaces.log.dvr.LogStatusDvrService;
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
public class DvrServiceImpl implements DvrService {
    private final Logger logger = Logger.getLogger(DvrService.class.getName());

    @Autowired
    private DvrRepository repository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private CameraRepository cameraRepository;
    @Autowired
    private DvrRelatorioRepository dvrRelatorioRepository;
    @Autowired
    private LogStatusDvrService logDvr;
    @Autowired
    private DvrDTOMapper mapper;
    @Autowired
    private DvrFindAllDTOMapper findAllDTOMapper;
    @Autowired
    private DataDvrDTOMapper dataMapper;
    @Autowired
    PagedResourcesAssembler<AllDvr> assembler;


    @Override
    public PagedModel<EntityModel<AllDvr>> findAll(Pageable pageable) {
        logger.info("Buscando os dvrs de forma paginada.");

        var dvr = repository.findAll(pageable);

        var dtoList = dvr.map(u -> dataMapper.apply(u));
        dtoList.forEach(u -> u.add(linkTo(methodOn(DvrController.class).findById(u.getId())).withSelfRel()));

        Link link = linkTo(methodOn(DvrController.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(dtoList, link);
    }

    @Override
    public List<FindAllDvr> findAll() {
        logger.info("Buscando todas os dvrs.");
        return repository.findAll()
                .stream().map(findAllDTOMapper)
                .toList();
    }

    @Override
    public ViewDvr findById(Long id) {
        logger.info("Buscando dvr pelo id: " + id);

        return repository.findById(id)
                .map(mapper)
                .orElseThrow(() -> new ObjectNotFoundException("Dvr de id: " + id + " não foi encontrado."));
    }

    @Override
    public ViewDvr add(AddDvr data) {
        logger.info("Adicionando dvr");
        if (repository.existsByNome(data.getNome()))
            throw new DataIntegratyViolationException("O dvr de nome enviado já existe.");

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));

        var dvr = new Dvr();
        dvr.setNome(data.getNome());
        dvr.setStatus(status);
        repository.save(dvr);

        var log = new AddLogStatusDvr();
        log.setDia(LocalDate.now());
        log.setAcao("CREATE");
        log.setStatusNovo(dvr.getStatus().getNome());
        log.setStatusAntigo("");
        log.setNomeNovo(dvr.getNome());
        log.setNomeAntigo("");
        log.setIdDvr(dvr.getId());
        logDvr.add(log);

        return mapper.apply(dvr);
    }

    @Override
    public ViewDvr update(UpdateDvr data) {
        logger.info("Atualizando dvr de id: " + data.getId());
        validUpdateDvr(data.getNome(), data.getId());

        var status = statusRepository.findById(data.getIdStatus())
                .orElseThrow(() -> new ObjectNotFoundException("O status de id: " + data.getIdStatus() + " não foi encontrado."));
        var dvrExistente = repository.findById(data.getId())
                .orElseThrow(() -> new ObjectNotFoundException("O dvr de id: " + data.getId() + " não foi encontrado para ser atualizado."));

        var log = new AddLogStatusDvr();
        log.setDia(data.getDia() == null ? LocalDate.now() : data.getDia());
        log.setAcao("UPDATE");
        log.setStatusNovo(status.getNome());
        log.setStatusAntigo(dvrExistente.getStatus().getNome());
        log.setNomeNovo(data.getNome());
        log.setNomeAntigo(dvrExistente.getNome());
        log.setIdDvr(dvrExistente.getId());
        logDvr.add(log);


        dvrExistente.setNome(data.getNome());
        dvrExistente.setStatus(status);

        return mapper.apply(repository.save(dvrExistente));
    }

    @Override
    public Boolean delete(Long id) {
        logger.info("Excluindo o dvr de id: " + id);
        var dvr = repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Dvr id: " + id + " não foi encontrada para ser deletado!"));

        if (!cameraRepository.findAllByDvrId(id).isEmpty())
            throw new DataIntegratyViolationException("O dvr não pode ser deletado, pois existem câmeras vinculadas!");

        if (!dvrRelatorioRepository.findAllByDvrId(id).isEmpty())
            throw new DataIntegratyViolationException("O dvr não pode ser deletado, pois existem relatórios de dvrs vinculados!");

        logDvr.deleteAllByDvr(id);
        repository.delete(dvr);
        return true;
    }

    private void validUpdateDvr(String nome, Long id) {
        var dvrByNome = repository.findByNome(nome);

        if (dvrByNome.isPresent() && !dvrByNome.get().getId().equals(id))
            throw new DataIntegratyViolationException("O nome: " + nome + " já foi cadastrado.");
    }

}
