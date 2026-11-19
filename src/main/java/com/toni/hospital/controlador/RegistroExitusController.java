package com.toni.hospital.controlador;

import com.toni.hospital.dto.AvisoFunerariaRequest;
import com.toni.hospital.dto.CertificadoExitusRequest;
import com.toni.hospital.dto.EntradaMorgueRequest;
import com.toni.hospital.dto.PacienteResumenResponse;
import com.toni.hospital.dto.RegistroExitusRequest;
import com.toni.hospital.dto.RegistroExitusResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.RetiradaMorgueRequest;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.modelo.NeveraMorgue;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.RegistroExitus;
import com.toni.hospital.servicio.RegistroExitusService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/registros-exitus")
public class RegistroExitusController {

    private final RegistroExitusService registroExitusService;

    public RegistroExitusController(
            RegistroExitusService registroExitusService
    ) {
        this.registroExitusService = registroExitusService;
    }

    /** Lista los registros de exitus. */
    @GetMapping
    public List<RegistroExitusResponse> listarRegistros() {
        return registroExitusService.listarRegistros()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca un registro por su ID. */
    @GetMapping("/{id}")
    public RegistroExitusResponse buscarPorId(@PathVariable Long id) {
        return convertirAResponse(
                registroExitusService.buscarPorId(id)
        );
    }

    /** Crea el registro inicial del exitus. */
    @PostMapping
    public ResponseEntity<RegistroExitusResponse> registrarExitus(
            @Valid @RequestBody RegistroExitusRequest request
    ) {
        RegistroExitus creado =
                registroExitusService.registrarExitus(request);

        // La ubicación apunta a GET /registros-exitus/{id}.
        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creado));
    }

    /** Registra el médico y la fecha del certificado. */
    @PatchMapping("/{id}/certificado")
    public RegistroExitusResponse registrarCertificado(
            @PathVariable Long id,
            @Valid @RequestBody CertificadoExitusRequest request
    ) {
        return convertirAResponse(
                registroExitusService.registrarCertificado(id, request)
        );
    }

    /** Registra la entrada del paciente en la morgue y la nevera asignada. */
    @PatchMapping("/{id}/entrada-morgue")
    public RegistroExitusResponse registrarEntradaMorgue(
            @PathVariable Long id,
            @Valid @RequestBody EntradaMorgueRequest request
    ) {
        return convertirAResponse(
                registroExitusService.registrarEntradaMorgue(id, request)
        );
    }

    /** Registra a qué funeraria se avisó y cuándo. */
    @PatchMapping("/{id}/aviso-funeraria")
    public RegistroExitusResponse registrarAvisoFuneraria(
            @PathVariable Long id,
            @Valid @RequestBody AvisoFunerariaRequest request
    ) {
        return convertirAResponse(
                registroExitusService.registrarAvisoFuneraria(id, request)
        );
    }

    /** Registra la fecha y hora de retirada por la funeraria. */
    @PatchMapping("/{id}/retirada")
    public RegistroExitusResponse registrarRetirada(
            @PathVariable Long id,
            @Valid @RequestBody RetiradaMorgueRequest request
    ) {
        return convertirAResponse(
                registroExitusService.registrarRetirada(id, request)
        );
    }

    /** Convierte la entidad JPA al DTO que devuelve la API. */
    private RegistroExitusResponse convertirAResponse(
            RegistroExitus registro
    ) {
        Paciente paciente = registro.getPaciente();

        PacienteResumenResponse pacienteResponse =
                new PacienteResumenResponse(
                        paciente.getId(),
                        paciente.getNombre(),
                        paciente.getApellidos()
                );

        ReferenciaResponse medicoResponse = null;
        if (registro.getMedicoCertificador() != null) {
            Medico medico = registro.getMedicoCertificador();

            medicoResponse = new ReferenciaResponse(
                    medico.getId(),
                    medico.getNombre() + " " + medico.getApellidos()
            );
        }

        ReferenciaResponse neveraResponse = null;
        if (registro.getNeveraMorgue() != null) {
            NeveraMorgue nevera = registro.getNeveraMorgue();

            neveraResponse = new ReferenciaResponse(
                    nevera.getId(),
                    nevera.getCodigo()
            );
        }

        return new RegistroExitusResponse(
                registro.getId(),
                pacienteResponse,
                registro.getAtencionUrgencias() != null
                        ? registro.getAtencionUrgencias().getId()
                        : null,
                registro.getIngresoHospitalario() != null
                        ? registro.getIngresoHospitalario().getId()
                        : null,
                registro.getFechaHoraFallecimiento(),
                medicoResponse,
                registro.getFechaHoraCertificadoMedico(),
                neveraResponse,
                registro.getFechaHoraEntradaMorgue(),
                registro.getNombreFuneraria(),
                registro.getFechaHoraAvisoFuneraria(),
                registro.getFechaHoraRetirada()
        );
    }
}