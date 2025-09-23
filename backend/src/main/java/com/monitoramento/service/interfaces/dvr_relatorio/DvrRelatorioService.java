package com.monitoramento.service.interfaces.dvr_relatorio;

import com.monitoramento.dto.dvr_relatorio.AddDvrRelatorio;
import com.monitoramento.dto.dvr_relatorio.AllDvrRelatorio;
import com.monitoramento.dto.dvr_relatorio.UpdateDvrRelatorio;
import com.monitoramento.dto.dvr_relatorio.ViewDvrRelatorio;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DvrRelatorioService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllDvrRelatorio>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    List<ViewDvrRelatorio> findAll();

    @Transactional(readOnly = true)
    ViewDvrRelatorio findById(Long id);

    @Transactional
    ViewDvrRelatorio add(AddDvrRelatorio data);

    @Transactional
    ViewDvrRelatorio update(UpdateDvrRelatorio data);

    @Transactional
    Boolean delete(Long id);

}
