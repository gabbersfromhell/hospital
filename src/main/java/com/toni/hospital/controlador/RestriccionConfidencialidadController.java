package com.toni.hospital.controlador;

import com.toni.hospital.dto.RestriccionConfidencialidadRequest;
import com.toni.hospital.dto.RestriccionConfidencialidadResponse;
import com.toni.hospital.modelo.RestriccionConfidencialidad;
import com.toni.hospital.servicio.RestriccionConfidencialidadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/** Endpoints para activar, consultar y finalizar restricciones. */
@RestController
@RequestMapping("/restricciones-confidencialidad")
public class RestriccionConfidencialidadController {

    private final RestriccionConfidencialidadService restriccionService;

    public RestriccionConfidencialidadController(
            RestriccionConfidencialidadService restriccionService
    ) {
        this.restriccionService = restriccionService;
    }

    @PostMapping
    public ResponseEntity<RestriccionConfidencialidadResponse> activar(
            @Valid @RequestBody RestriccionConfidencialidadRequest request
    ) {
        RestriccionConfidencialidad creada =
                restriccionService.activar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creada));
    }

    @GetMapping("/{id}")
    public RestriccionConfidencialidadResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(
                restriccionService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable Long id) {
        restriccionService.finalizar(id);
        return ResponseEntity.noContent().build();
    }

    private RestriccionConfidencialidadResponse convertirAResponse(
            RestriccionConfidencialidad restriccion
    ) {
        boolean activa = restriccion.getFechaHoraFin() == null;

        return new RestriccionConfidencialidadResponse(
                restriccion.getId(),
                restriccion.getFechaHoraInicio(),
                restriccion.getFechaHoraFin(),
                activa
        );
    }
}