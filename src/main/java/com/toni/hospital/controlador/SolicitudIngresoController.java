package com.toni.hospital.controlador;

import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.SolicitudIngresoRequest;
import com.toni.hospital.dto.SolicitudIngresoResponse;
import com.toni.hospital.modelo.IngresoHospitalario;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.modelo.SolicitudIngreso;
import com.toni.hospital.servicio.SolicitudIngresoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints de solicitudes de ingreso vinculadas a una atención.
 */
@RestController
@RequestMapping(
        "/atenciones-urgencias/{atencionId}/solicitudes-ingreso"
)
public class SolicitudIngresoController {

    private final SolicitudIngresoService solicitudIngresoService;

    public SolicitudIngresoController(
            SolicitudIngresoService solicitudIngresoService
    ) {
        this.solicitudIngresoService = solicitudIngresoService;
    }

    @GetMapping
    public List<SolicitudIngresoResponse> listarPorAtencion(
            @PathVariable Long atencionId
    ) {
        return solicitudIngresoService.listarPorAtencion(atencionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public SolicitudIngresoResponse buscarPorId(
            @PathVariable Long atencionId,
            @PathVariable Long id
    ) {
        return convertirAResponse(
                solicitudIngresoService.buscarPorAtencion(atencionId, id)
        );
    }

    @PostMapping
    public ResponseEntity<SolicitudIngresoResponse> crearSolicitud(
            @PathVariable Long atencionId,
            @Valid @RequestBody SolicitudIngresoRequest request
    ) {
        SolicitudIngreso creada =
                solicitudIngresoService.crearSolicitud(atencionId, request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creada));
    }

    private SolicitudIngresoResponse convertirAResponse(
            SolicitudIngreso solicitud
    ) {
        Medico medico = solicitud.getMedicoSolicitante();
        ServicioHospitalario servicio = solicitud.getServicioHospitalario();
        IngresoHospitalario ingreso = solicitud.getIngresoHospitalario();

        ReferenciaResponse medicoResponse = medico == null
                ? null
                : new ReferenciaResponse(
                medico.getId(),
                medico.getNombre() + " " + medico.getApellidos()
        );

        ReferenciaResponse servicioResponse = servicio == null
                ? null
                : new ReferenciaResponse(
                servicio.getId(),
                servicio.getNombre()
        );

        Long ingresoId = ingreso == null ? null : ingreso.getId();

        return new SolicitudIngresoResponse(
                solicitud.getId(),
                solicitud.getAtencionUrgencias().getId(),
                medicoResponse,
                servicioResponse,
                solicitud.getFechaSolicitud(),
                solicitud.getMotivo(),
                solicitud.getEstado(),
                ingresoId
        );
    }
}