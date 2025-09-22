package com.monitoramento.controller.log.dvr;

import com.monitoramento.dto.log.dvr.AddLogStatusDvr;
import com.monitoramento.dto.log.dvr.AllLogStatusDvr;
import com.monitoramento.dto.log.dvr.ViewLogStatusDvr;
import com.monitoramento.service.interfaces.log.dvr.LogStatusDvrService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log-dvr")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LogStatusDvrController {

    @Autowired
    private LogStatusDvrService service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<AllLogStatusDvr>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "dvr.id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/dvr/{dvrId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<AllLogStatusDvr>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long dvrId
    ) {
        var sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "dvr.id"));
        return ResponseEntity.ok(service.findAllByDvrId(pageable, dvrId));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewLogStatusDvr> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewLogStatusDvr> add(@Valid @RequestBody AddLogStatusDvr request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(request));

    }

}
