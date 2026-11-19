package com.toni.hospital.controlador;

import com.toni.hospital.dto.JustificanteAsistenciaResponse;
import com.toni.hospital.dto.PacienteResumenResponse;
import com.toni.hospital.modelo.JustificanteAsistencia;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.servicio.JustificanteAsistenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints administrativos de justificantes de asistencia.
 * No devuelven diagnósticos ni contenido del informe clínico.
 */
@RestController
@RequestMapping(
        "/atenciones-urgencias/{atencionId}/justificantes"
)
public class JustificanteAsistenciaController {

    private final JustificanteAsistenciaService justificanteService;

    public JustificanteAsistenciaController(
            JustificanteAsistenciaService justificanteService
    ) {
        this.justificanteService = justificanteService;
    }

    @GetMapping
    public List<JustificanteAsistenciaResponse> listarPorAtencion(
            @PathVariable Long atencionId
    ) {
        return justificanteService.listarPorAtencion(atencionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{justificanteId}")
    public JustificanteAsistenciaResponse buscarPorId(
            @PathVariable Long atencionId,
            @PathVariable Long justificanteId
    ) {
        return convertirAResponse(
                justificanteService.buscarPorAtencion(
                        atencionId,
                        justificanteId
                )
        );
    }

    @PostMapping
    public ResponseEntity<JustificanteAsistenciaResponse> emitir(
            @PathVariable Long atencionId
    ) {
        JustificanteAsistencia emitido =
                justificanteService.emitir(atencionId);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{justificanteId}")
                .buildAndExpand(emitido.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(emitido));
    }

    private JustificanteAsistenciaResponse convertirAResponse(
            JustificanteAsistencia justificante
    ) {
        var atencion = justificante.getAtencionUrgencias();
        Paciente paciente = atencion.getPaciente();

        PacienteResumenResponse pacienteResponse =
                new PacienteResumenResponse(
                        paciente.getId(),
                        paciente.getNombre(),
                        paciente.getApellidos()
                );

        return new JustificanteAsistenciaResponse(
                justificante.getId(),
                atencion.getId(),
                justificante.getFechaHoraEmision(),
                atencion.getFechaHoraLlegada(),
                atencion.getFechaHoraFinUrgencias(),
                pacienteResponse
        );
    }
}