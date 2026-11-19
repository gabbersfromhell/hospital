package com.toni.hospital.controlador;

import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.UbicacionUrgenciasRequest;
import com.toni.hospital.dto.UbicacionUrgenciasResponse;
import com.toni.hospital.modelo.UbicacionUrgencias;
import com.toni.hospital.servicio.UbicacionUrgenciasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ubicaciones-urgencias")
public class UbicacionUrgenciasController {

    private final UbicacionUrgenciasService ubicacionService;

    public UbicacionUrgenciasController(
            UbicacionUrgenciasService ubicacionService
    ) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public List<UbicacionUrgenciasResponse> listarUbicaciones() {
        return ubicacionService.listarUbicaciones()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/activas")
    public List<UbicacionUrgenciasResponse> listarUbicacionesActivas() {
        return ubicacionService.listarUbicacionesActivas()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UbicacionUrgenciasResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(
                ubicacionService.buscarUbicacionPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<UbicacionUrgenciasResponse> guardarUbicacion(
            @Valid @RequestBody UbicacionUrgenciasRequest request
    ) {
        UbicacionUrgencias guardada =
                ubicacionService.guardarUbicacion(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    @PutMapping("/{id}")
    public UbicacionUrgenciasResponse actualizarUbicacion(
            @PathVariable Long id,
            @Valid @RequestBody UbicacionUrgenciasRequest request
    ) {
        return convertirAResponse(
                ubicacionService.actualizarUbicacion(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUbicacion(
            @PathVariable Long id
    ) {
        ubicacionService.desactivarUbicacion(id);
        return ResponseEntity.noContent().build();
    }

    private UbicacionUrgenciasResponse convertirAResponse(
            UbicacionUrgencias ubicacion
    ) {
        ReferenciaResponse hospital = new ReferenciaResponse(
                ubicacion.getHospital().getId(),
                ubicacion.getHospital().getNombre()
        );

        return new UbicacionUrgenciasResponse(
                ubicacion.getId(),
                ubicacion.getCodigo(),
                ubicacion.getTipo(),
                ubicacion.isActiva(),
                hospital
        );
    }
}