package com.toni.hospital.controlador;

import com.toni.hospital.dto.AdministracionMedicacionResponse;
import com.toni.hospital.dto.ProgramarAdministracionMedicacionRequest;
import com.toni.hospital.dto.RegistrarAdministracionMedicacionRequest;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.AdministracionMedicacion;
import com.toni.hospital.servicio.AdministracionMedicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para programar y registrar administraciones de medicación. */
@RestController
@RequestMapping("/administraciones-medicacion")
public class AdministracionMedicacionController {

    private final AdministracionMedicacionService administracionService;

    public AdministracionMedicacionController(
            AdministracionMedicacionService administracionService
    ) {
        this.administracionService = administracionService;
    }

    /** Lista las dosis programadas para una prescripción. */
    @GetMapping("/prescripcion/{prescripcionId}")
    public List<AdministracionMedicacionResponse> listarPorPrescripcion(
            @PathVariable Long prescripcionId
    ) {
        return administracionService
                .listarPorPrescripcion(prescripcionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Consulta una administración por ID. */
    @GetMapping("/{id}")
    public AdministracionMedicacionResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(administracionService.buscarPorId(id));
    }

    /** Programa una dosis y responde con 201 Created. */
    @PostMapping
    public ResponseEntity<AdministracionMedicacionResponse> programar(
            @Valid @RequestBody
            ProgramarAdministracionMedicacionRequest request
    ) {
        AdministracionMedicacion programada =
                administracionService.programar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(programada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(programada));
    }

    /** Registra si una dosis programada se administró o no. */
    @PatchMapping("/{id}/resultado")
    public AdministracionMedicacionResponse registrarResultado(
            @PathVariable Long id,
            @Valid @RequestBody
            RegistrarAdministracionMedicacionRequest request
    ) {
        AdministracionMedicacion actualizada =
                administracionService.registrarResultado(id, request);

        return convertirAResponse(actualizada);
    }

    private AdministracionMedicacionResponse convertirAResponse(
            AdministracionMedicacion administracion
    ) {
        ReferenciaResponse medicamento = new ReferenciaResponse(
                administracion.getPrescripcionMedicacion()
                        .getMedicamento().getId(),
                administracion.getPrescripcionMedicacion()
                        .getMedicamento().getNombreGenerico()
        );

        return new AdministracionMedicacionResponse(
                administracion.getId(),
                administracion.getPrescripcionMedicacion().getId(),
                medicamento,
                administracion.getFechaHoraProgramada(),
                administracion.getFechaHoraAdministracion(),
                administracion.getFechaHoraRegistroResultado(),
                administracion.getEstado(),
                administracion.getDosisAdministrada(),
                administracion.getUnidadDosis(),
                administracion.getEnfermeroResponsable() == null
                        ? null
                        : administracion.getEnfermeroResponsable().getId(),
                administracion.getMotivoNoAdministracion(),
                administracion.getObservaciones()
        );
    }
}