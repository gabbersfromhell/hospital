package com.toni.hospital.controlador;

import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.SolicitudPruebaDiagnosticaRequest;
import com.toni.hospital.dto.SolicitudPruebaDiagnosticaResponse;
import com.toni.hospital.modelo.SolicitudPruebaDiagnostica;
import com.toni.hospital.servicio.SolicitudPruebaDiagnosticaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para consultar y registrar solicitudes diagnósticas. */
@RestController
@RequestMapping("/solicitudes-pruebas-diagnosticas")
public class SolicitudPruebaDiagnosticaController {

    private final SolicitudPruebaDiagnosticaService solicitudService;

    public SolicitudPruebaDiagnosticaController(
            SolicitudPruebaDiagnosticaService solicitudService
    ) {
        this.solicitudService = solicitudService;
    }

    /** Lista las solicitudes registradas. */
    @GetMapping
    public List<SolicitudPruebaDiagnosticaResponse> listarSolicitudes() {
        return solicitudService.listarSolicitudes()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca una solicitud por su ID. */
    @GetMapping("/{id}")
    public SolicitudPruebaDiagnosticaResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(solicitudService.buscarPorId(id));
    }

    /** Registra una solicitud y responde con 201 Created. */
    @PostMapping
    public ResponseEntity<SolicitudPruebaDiagnosticaResponse> guardar(
            @Valid @RequestBody SolicitudPruebaDiagnosticaRequest request
    ) {
        SolicitudPruebaDiagnostica guardada =
                solicitudService.guardar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    /** Convierte la entidad a los datos de respuesta de la API. */
    private SolicitudPruebaDiagnosticaResponse convertirAResponse(
            SolicitudPruebaDiagnostica solicitud
    ) {
        ReferenciaResponse servicioDestino = new ReferenciaResponse(
                solicitud.getServicioDestino().getId(),
                solicitud.getServicioDestino().getNombre()
        );

        return new SolicitudPruebaDiagnosticaResponse(
                solicitud.getId(),
                servicioDestino,
                solicitud.getTipo(),
                solicitud.getNombrePrueba(),
                solicitud.getFechaHoraSolicitud(),
                solicitud.getIndicacionClinica(),
                solicitud.getEstado(),
                solicitud.getTipoProfesionalSolicitante(),
                solicitud.getDetalleOtroProfesional(),
                solicitud.getAtencionUrgencias() == null
                        ? null
                        : solicitud.getAtencionUrgencias().getId(),
                solicitud.getIngresoHospitalario() == null
                        ? null
                        : solicitud.getIngresoHospitalario().getId(),
                solicitud.getCitaAmbulatoria() == null
                        ? null
                        : solicitud.getCitaAmbulatoria().getId()
        );
    }
}