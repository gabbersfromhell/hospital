package com.toni.hospital.controlador;

import com.toni.hospital.dto.AsignacionCamaRequest;
import com.toni.hospital.dto.AsignacionCamaResponse;
import com.toni.hospital.dto.CamaResumenResponse;
import com.toni.hospital.modelo.AsignacionCama;
import com.toni.hospital.modelo.Cama;
import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.modelo.Planta;
import com.toni.hospital.servicio.AsignacionCamaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ingresos-hospitalarios/{ingresoId}/asignaciones-cama")
public class AsignacionCamaController {

    private final AsignacionCamaService asignacionCamaService;

    public AsignacionCamaController(
            AsignacionCamaService asignacionCamaService
    ) {
        this.asignacionCamaService = asignacionCamaService;
    }

    /** Lista el historial de camas de un ingreso. */
    @GetMapping
    public List<AsignacionCamaResponse> listarPorIngreso(
            @PathVariable Long ingresoId
    ) {
        return asignacionCamaService.listarPorIngreso(ingresoId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca una asignación concreta dentro del ingreso indicado. */
    @GetMapping("/{asignacionId}")
    public AsignacionCamaResponse buscarPorIngreso(
            @PathVariable Long ingresoId,
            @PathVariable Long asignacionId
    ) {
        AsignacionCama asignacion =
                asignacionCamaService.buscarPorIngreso(
                        ingresoId,
                        asignacionId
                );

        return convertirAResponse(asignacion);
    }

    /** Asigna una cama al ingreso y devuelve HTTP 201 Created. */
    @PostMapping
    public ResponseEntity<AsignacionCamaResponse> asignarCama(
            @PathVariable Long ingresoId,
            @Valid @RequestBody AsignacionCamaRequest request
    ) {
        AsignacionCama asignacion =
                asignacionCamaService.asignarCama(ingresoId, request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{asignacionId}")
                .buildAndExpand(asignacion.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(asignacion));
    }

    private AsignacionCamaResponse convertirAResponse(
            AsignacionCama asignacion
    ) {
        Cama cama = asignacion.getCama();
        Habitacion habitacion = cama.getHabitacion();
        Planta planta = habitacion.getPlanta();

        CamaResumenResponse camaResponse = new CamaResumenResponse(
                cama.getId(),
                cama.getNumeroCama(),
                habitacion.getNumeroHabitacion(),
                planta.getNumeroPlanta()
        );

        return new AsignacionCamaResponse(
                asignacion.getId(),
                asignacion.getIngresoHospitalario().getId(),
                camaResponse,
                asignacion.getFechaHoraInicio(),
                asignacion.getFechaHoraFin(),
                asignacion.getMotivo()
        );
    }
}