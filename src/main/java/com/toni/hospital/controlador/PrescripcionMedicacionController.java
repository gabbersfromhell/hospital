package com.toni.hospital.controlador;

import com.toni.hospital.dto.PrescripcionMedicacionRequest;
import com.toni.hospital.dto.PrescripcionMedicacionResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.PrescripcionMedicacion;
import com.toni.hospital.servicio.PrescripcionMedicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para consultar y registrar prescripciones. */
@RestController
@RequestMapping("/prescripciones-medicacion")
public class PrescripcionMedicacionController {

    private final PrescripcionMedicacionService prescripcionService;

    public PrescripcionMedicacionController(
            PrescripcionMedicacionService prescripcionService
    ) {
        this.prescripcionService = prescripcionService;
    }

    /** Lista las prescripciones registradas. */
    @GetMapping
    public List<PrescripcionMedicacionResponse> listar() {
        return prescripcionService.listarPrescripciones()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca una prescripción por su ID. */
    @GetMapping("/{id}")
    public PrescripcionMedicacionResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(prescripcionService.buscarPorId(id));
    }

    /** Registra una prescripción y responde con 201 Created. */
    @PostMapping
    public ResponseEntity<PrescripcionMedicacionResponse> guardar(
            @Valid @RequestBody PrescripcionMedicacionRequest request
    ) {
        PrescripcionMedicacion guardada =
                prescripcionService.guardar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    private PrescripcionMedicacionResponse convertirAResponse(
            PrescripcionMedicacion prescripcion
    ) {
        ReferenciaResponse medicamento = new ReferenciaResponse(
                prescripcion.getMedicamento().getId(),
                prescripcion.getMedicamento().getNombreGenerico()
        );

        return new PrescripcionMedicacionResponse(
                prescripcion.getId(),
                medicamento,
                prescripcion.getMedicoPrescriptor().getId(),
                prescripcion.getDosisPorAdministracion(),
                prescripcion.getUnidadDosis(),
                prescripcion.getViaAdministracion(),
                prescripcion.getPauta(),
                prescripcion.isSiPrecisa(),
                prescripcion.getIndicacionSiPrecisa(),
                prescripcion.getIndicacionClinica(),
                prescripcion.getFechaHoraPrescripcion(),
                prescripcion.getFechaHoraInicio(),
                prescripcion.getFechaHoraFin(),
                prescripcion.getEstado(),
                prescripcion.getAtencionUrgencias() == null
                        ? null
                        : prescripcion.getAtencionUrgencias().getId(),
                prescripcion.getIngresoHospitalario() == null
                        ? null
                        : prescripcion.getIngresoHospitalario().getId(),
                prescripcion.getCitaAmbulatoria() == null
                        ? null
                        : prescripcion.getCitaAmbulatoria().getId()
        );
    }
}