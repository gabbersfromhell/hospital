package com.toni.hospital.controlador;

import com.toni.hospital.dto.AltaHospitalariaRequest;
import com.toni.hospital.dto.AsignacionCamaResponse;
import com.toni.hospital.dto.CamaResumenResponse;
import com.toni.hospital.dto.IngresoHospitalarioResponse;
import com.toni.hospital.modelo.AsignacionCama;
import com.toni.hospital.modelo.Cama;
import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.modelo.IngresoHospitalario;
import com.toni.hospital.modelo.Planta;
import com.toni.hospital.servicio.IngresoHospitalarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/ingresos-hospitalarios")
public class IngresoHospitalarioController {

    private final IngresoHospitalarioService ingresoHospitalarioService;

    public IngresoHospitalarioController(
            IngresoHospitalarioService ingresoHospitalarioService
    ) {
        this.ingresoHospitalarioService = ingresoHospitalarioService;
    }

    @GetMapping
    public List<IngresoHospitalarioResponse> listarIngresos() {
        return ingresoHospitalarioService.listarIngresos()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public IngresoHospitalarioResponse buscarIngresoPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(
                ingresoHospitalarioService.buscarIngresoPorId(id)
        );
    }

    /**
     * POST /ingresos-hospitalarios/desde-solicitud/{solicitudId}
     */
    @PostMapping("/desde-solicitud/{solicitudId}")
    public ResponseEntity<IngresoHospitalarioResponse> crearDesdeSolicitud(
            @PathVariable Long solicitudId
    ) {
        IngresoHospitalario creado =
                ingresoHospitalarioService.crearDesdeSolicitud(solicitudId);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/ingresos-hospitalarios/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creado));
    }

    /**
     * PUT /ingresos-hospitalarios/{id}/alta
     */
    @PutMapping("/{id}/alta")
    public IngresoHospitalarioResponse registrarAlta(
            @PathVariable Long id,
            @Valid @RequestBody AltaHospitalariaRequest request
    ) {
        return convertirAResponse(
                ingresoHospitalarioService.registrarAlta(id, request)
        );
    }

    private IngresoHospitalarioResponse convertirAResponse(
            IngresoHospitalario ingreso
    ) {
        List<AsignacionCamaResponse> asignaciones =
                ingreso.getAsignacionesCama()
                        .stream()
                        .map(this::convertirAsignacionAResponse)
                        .toList();

        return new IngresoHospitalarioResponse(
                ingreso.getId(),
                ingreso.getSolicitudIngreso().getId(),
                ingreso.getFechaHoraIngreso(),
                ingreso.getFechaHoraAlta(),
                ingreso.getTipoAlta(),
                asignaciones
        );
    }

    private AsignacionCamaResponse convertirAsignacionAResponse(
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