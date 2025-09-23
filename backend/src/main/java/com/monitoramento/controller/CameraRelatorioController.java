package com.monitoramento.controller;

import com.monitoramento.dto.camera_relatorio.AddCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.AllCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.UpdateCameraRelatorio;
import com.monitoramento.dto.camera_relatorio.ViewCameraRelatorio;
import com.monitoramento.service.interfaces.camera_relatorio.CameraRelatorioService;
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
@RequestMapping("/api/camera-relatorio")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CameraRelatorioController {

    @Autowired
    private CameraRelatorioService service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<AllCameraRelatorio>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewCameraRelatorio> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewCameraRelatorio> add(@Valid @RequestBody AddCameraRelatorio request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(request));

    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewCameraRelatorio> update(@Valid @RequestBody UpdateCameraRelatorio request) {
        return ResponseEntity.ok().body(service.update(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
