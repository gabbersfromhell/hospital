package com.toni.hospital.controlador;

import com.toni.hospital.dto.InformeRadiologicoRequest;
import com.toni.hospital.dto.InformeRadiologicoResponse;
import com.toni.hospital.modelo.InformeRadiologico;
import com.toni.hospital.servicio.InformeRadiologicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para consultar y registrar informes radiológicos. */
@RestController
@RequestMapping("/informes-radiologicos")
public class InformeRadiologicoController {

    private final InformeRadiologicoService informeService;

    public InformeRadiologicoController(
            InformeRadiologicoService informeService
    ) {
        this.informeService = informeService;
    }

    /** Lista los informes asociados a una solicitud diagnóstica. */
    @GetMapping("/solicitud/{solicitudId}")
    public List<InformeRadiologicoResponse> listarPorSolicitud(
            @PathVariable Long solicitudId
    ) {
        return informeService.listarPorSolicitud(solicitudId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca un informe por su ID. */
    @GetMapping("/{id}")
    public InformeRadiologicoResponse buscarPorId(@PathVariable Long id) {
        return convertirAResponse(informeService.buscarPorId(id));
    }

    /** Registra un informe y responde con 201 Created. */
    @PostMapping
    public ResponseEntity<InformeRadiologicoResponse> guardar(
            @Valid @RequestBody InformeRadiologicoRequest request
    ) {
        InformeRadiologico guardado = informeService.guardar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardado));
    }

    private InformeRadiologicoResponse convertirAResponse(
            InformeRadiologico informe
    ) {
        return new InformeRadiologicoResponse(
                informe.getId(),
                informe.getSolicitudPrueba().getId(),
                informe.getMedicoInformante().getId(),
                informe.getFechaHoraEstudio(),
                informe.getFechaHoraInforme(),
                informe.getHallazgos(),
                informe.getConclusion(),
                informe.getEstado()
        );
    }
}