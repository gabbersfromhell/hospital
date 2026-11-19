package com.toni.hospital.controlador;

import com.toni.hospital.dto.ContactoPacienteResponse;
import com.toni.hospital.dto.PacienteRequest;
import com.toni.hospital.dto.PacienteResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.ContactoPaciente;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.Poblacion;
import com.toni.hospital.servicio.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    /**
     * GET /pacientes
     * Devuelve la lista de pacientes como respuestas DTO.
     */
    @GetMapping
    public List<PacienteResponse> listarPacientes() {
        return pacienteService.listarPacientes()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * GET /pacientes/{id}
     * Busca un paciente por su identificador.
     */
    @GetMapping("/{id}")
    public PacienteResponse buscarPacientePorId(@PathVariable Long id) {
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        return convertirAResponse(paciente);
    }

    /**
     * POST /pacientes
     * Recibe los datos del paciente, lo guarda y responde con HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<PacienteResponse> guardarPaciente(
            @Valid @RequestBody PacienteRequest request
    ) {
        Paciente guardado = pacienteService.guardarPaciente(request);

        // Construye la dirección del paciente recién creado: /pacientes/{id}.
        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardado));
    }

    /**
     * Convierte la entidad JPA a un DTO de respuesta.
     * Así la API decide qué información exponer al cliente.
     */
    private PacienteResponse convertirAResponse(Paciente paciente) {
        ReferenciaResponse poblacionResponse = convertirPoblacionAReferencia(
                paciente.getPoblacion()
        );

        List<ContactoPacienteResponse> contactosResponse =
                paciente.getContactos()
                        .stream()
                        .map(this::convertirContactoAResponse)
                        .toList();

        return new PacienteResponse(
                paciente.getId(),
                paciente.getNombre(),
                paciente.getApellidos(),
                paciente.getTipoDocumento(),
                paciente.getNumeroDocumento(),
                paciente.getCip(),
                paciente.getDireccion(),
                paciente.getCorreoElectronico(),
                poblacionResponse,
                contactosResponse
        );
    }

    /**
     * PUT /pacientes/{id}
     * Actualiza los datos del paciente y devuelve su representación actualizada.
     */
    @PutMapping("/{id}")
    public PacienteResponse actualizarPaciente(
            @PathVariable Long id,
            @Valid @RequestBody PacienteRequest request
    ) {
        Paciente actualizado = pacienteService.actualizarPaciente(id, request);
        return convertirAResponse(actualizado);
    }


    /**
     * Devuelve solo el ID y el nombre de la población asociada.
     */
    private ReferenciaResponse convertirPoblacionAReferencia(
            Poblacion poblacion
    ) {
        if (poblacion == null) {
            return null;
        }

        return new ReferenciaResponse(
                poblacion.getId(),
                poblacion.getNombre()
        );
    }

    /**
     * Convierte un contacto asociado al paciente a su DTO de respuesta.
     */
    private ContactoPacienteResponse convertirContactoAResponse(
            ContactoPaciente contacto
    ) {
        return new ContactoPacienteResponse(
                contacto.getId(),
                contacto.getNombre(),
                contacto.getApellidos(),
                contacto.getRelacionConPaciente(),
                contacto.getTelefono(),
                contacto.isContactoPrincipal()
        );
    }
}