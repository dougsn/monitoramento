package com.monitoramento.controller.log.camera;

import com.monitoramento.dto.log.camera.AddLogStatusCamera;
import com.monitoramento.dto.log.camera.AllLogStatusCamera;
import com.monitoramento.dto.log.camera.ViewLogStatusCamera;
import com.monitoramento.service.interfaces.log.camera.LogStatusCameraService;
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
@RequestMapping("/api/log-camera")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LogStatusCameraController {

    @Autowired
    private LogStatusCameraService service;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<AllLogStatusCamera>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "camera.id"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/camera/{cameraId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PagedModel<EntityModel<AllLogStatusCamera>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @PathVariable Long cameraId
    ) {
        var sortDirection = "asc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "camera.id"));
        return ResponseEntity.ok(service.findAllByCameraId(pageable, cameraId));
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewLogStatusCamera> findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ViewLogStatusCamera> add(@Valid @RequestBody AddLogStatusCamera request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.add(request));
    }

}
