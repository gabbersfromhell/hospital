package com.toni.hospital.controlador;

import com.toni.hospital.dto.AtencionUrgenciasRequest;
import com.toni.hospital.dto.AtencionUrgenciasResponse;
import com.toni.hospital.dto.CerrarAtencionUrgenciasRequest;
import com.toni.hospital.dto.PacienteResumenResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.UbicacionUrgencias;
import com.toni.hospital.servicio.AtencionUrgenciasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints para consultar y gestionar atenciones de Urgencias.
 */
@RestController
@RequestMapping("/atenciones-urgencias")
public class AtencionUrgenciasController {

    private final AtencionUrgenciasService atencionUrgenciasService;

    public AtencionUrgenciasController(
            AtencionUrgenciasService atencionUrgenciasService
    ) {
        this.atencionUrgenciasService = atencionUrgenciasService;
    }

    /**
     * GET /atenciones-urgencias
     * Devuelve todas las atenciones.
     */
    @GetMapping
    public List<AtencionUrgenciasResponse> listarAtenciones() {
        return atencionUrgenciasService.listarAtenciones()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * GET /atenciones-urgencias/activas
     * Devuelve el censo de pacientes que siguen en Urgencias.
     */
    @GetMapping("/activas")
    public List<AtencionUrgenciasResponse> listarAtencionesActivas() {
        return atencionUrgenciasService.listarAtencionesActivas()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * GET /atenciones-urgencias/{id}
     * Busca una atención por su ID.
     */
    @GetMapping("/{id}")
    public AtencionUrgenciasResponse buscarAtencionPorId(
            @PathVariable Long id
    ) {
        AtencionUrgencias atencion =
                atencionUrgenciasService.buscarAtencionPorId(id);

        return convertirAResponse(atencion);
    }

    /**
     * POST /atenciones-urgencias
     * Registra una atención y responde con HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<AtencionUrgenciasResponse> crearAtencion(
            @Valid @RequestBody AtencionUrgenciasRequest request
    ) {
        AtencionUrgencias creada =
                atencionUrgenciasService.crearAtencion(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creada));
    }

    /**
     * POST /atenciones-urgencias/{id}/cerrar
     * Cierra la atención y registra el resultado.
     */
    @PostMapping("/{id}/cerrar")
    public AtencionUrgenciasResponse cerrarAtencion(
            @PathVariable Long id,
            @Valid @RequestBody CerrarAtencionUrgenciasRequest request
    ) {
        AtencionUrgencias cerrada =
                atencionUrgenciasService.cerrarAtencion(id, request);

        return convertirAResponse(cerrada);
    }

    /**
     * Convierte la entidad JPA en el DTO de respuesta.
     */
    private AtencionUrgenciasResponse convertirAResponse(
            AtencionUrgencias atencion
    ) {
        return new AtencionUrgenciasResponse(
                atencion.getId(),
                atencion.getFechaHoraLlegada(),
                atencion.getFechaHoraPrimeraAsistenciaMedica(),
                atencion.getFechaHoraFinUrgencias(),
                atencion.getMotivoConsulta(),
                atencion.getCircuitoUrgencias(),
                atencion.getResultado(),
                convertirHospitalAReferencia(atencion.getHospitalAtencion()),
                convertirUbicacionAReferencia(atencion.getUbicacionActual()),
                atencion.getTipoCobertura(),
                atencion.getEntidadCobertura(),
                convertirPacienteAResumen(atencion.getPaciente()),
                atencion.getVinculoProfesional(),
                atencion.isAccidenteLaboral(),
                atencion.isBajoCustodiaPolicial()
        );
    }

    private ReferenciaResponse convertirHospitalAReferencia(
            Hospital hospital
    ) {
        if (hospital == null) {
            return null;
        }

        return new ReferenciaResponse(
                hospital.getId(),
                hospital.getNombre()
        );
    }

    private ReferenciaResponse convertirUbicacionAReferencia(
            UbicacionUrgencias ubicacion
    ) {
        if (ubicacion == null) {
            return null;
        }

        return new ReferenciaResponse(
                ubicacion.getId(),
                ubicacion.getCodigo()
        );
    }

    private PacienteResumenResponse convertirPacienteAResumen(
            Paciente paciente
    ) {
        if (paciente == null) {
            return null;
        }

        return new PacienteResumenResponse(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellidos()
        );
    }
}