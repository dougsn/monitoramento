package com.monitoramento.service.interfaces.relatorio;

import com.monitoramento.dto.relatorio.AddRelatorio;
import com.monitoramento.dto.relatorio.AllRelatorio;
import com.monitoramento.dto.relatorio.UpdateRelatorio;
import com.monitoramento.dto.relatorio.ViewRelatorio;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface RelatorioService {

    @Transactional(readOnly = true)
    PagedModel<EntityModel<AllRelatorio>> findAll(Pageable pageable);

    @Transactional(readOnly = true)
    ViewRelatorio findById(Long id);

    @Transactional
    ViewRelatorio add(AddRelatorio data);

    @Transactional
    ViewRelatorio update(UpdateRelatorio data);

    @Transactional
    Boolean delete(Long id);

}
