package com.toni.hospital.controlador;

import com.toni.hospital.dto.CitaAmbulatoriaRequest;
import com.toni.hospital.dto.CitaAmbulatoriaResponse;
import com.toni.hospital.dto.PacienteResumenResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.CitaAmbulatoria;
import com.toni.hospital.servicio.CitaAmbulatoriaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/citas-ambulatorias")
public class CitaAmbulatoriaController {

    private final CitaAmbulatoriaService citaService;

    public CitaAmbulatoriaController(CitaAmbulatoriaService citaService) {
        this.citaService = citaService;
    }

    /** Devuelve todas las citas ambulatorias. */
    @GetMapping
    public List<CitaAmbulatoriaResponse> listarCitas() {
        return citaService.listarCitas()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca una cita por su ID. */
    @GetMapping("/{id}")
    public CitaAmbulatoriaResponse buscarCitaPorId(@PathVariable Long id) {
        CitaAmbulatoria cita = citaService.buscarCitaPorId(id);
        return convertirAResponse(cita);
    }

    /** Programa una cita y responde con estado 201 Created. */
    @PostMapping
    public ResponseEntity<CitaAmbulatoriaResponse> guardarCita(
            @Valid @RequestBody CitaAmbulatoriaRequest request
    ) {
        CitaAmbulatoria guardada = citaService.guardarCita(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    /** Convierte la entidad en el DTO que recibirá el cliente. */
    private CitaAmbulatoriaResponse convertirAResponse(CitaAmbulatoria cita) {
        PacienteResumenResponse paciente = new PacienteResumenResponse(
                cita.getPaciente().getId(),
                cita.getPaciente().getNombre(),
                cita.getPaciente().getApellidos()
        );

        ReferenciaResponse hospital = new ReferenciaResponse(
                cita.getHospital().getId(),
                cita.getHospital().getNombre()
        );

        ReferenciaResponse servicio = new ReferenciaResponse(
                cita.getServicio().getId(),
                cita.getServicio().getNombre()
        );

        return new CitaAmbulatoriaResponse(
                cita.getId(),
                paciente,
                hospital,
                servicio,
                cita.getTipo(),
                cita.getFechaHoraProgramada(),
                cita.getFechaHoraInicioReal(),
                cita.getFechaHoraFinReal(),
                cita.getEstado(),
                cita.getMotivo(),
                cita.getObservaciones()
        );
    }
}