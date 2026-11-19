package com.toni.hospital.controlador;

import com.toni.hospital.dto.ContactoPacienteRequest;
import com.toni.hospital.dto.ContactoPacienteResponse;
import com.toni.hospital.modelo.ContactoPaciente;
import com.toni.hospital.servicio.ContactoPacienteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints para gestionar los contactos de un paciente.
 */
@RestController
@RequestMapping("/pacientes/{pacienteId}/contactos")
public class ContactoPacienteController {

    private final ContactoPacienteService contactoPacienteService;

    public ContactoPacienteController(
            ContactoPacienteService contactoPacienteService
    ) {
        this.contactoPacienteService = contactoPacienteService;
    }

    /**
     * GET /pacientes/{pacienteId}/contactos
     * Lista los contactos de un paciente.
     */
    @GetMapping
    public List<ContactoPacienteResponse> listarContactos(
            @PathVariable Long pacienteId
    ) {
        return contactoPacienteService.listarContactos(pacienteId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * POST /pacientes/{pacienteId}/contactos
     * Añade un contacto y responde con HTTP 201 Created.
     */
    @PostMapping
    public ResponseEntity<ContactoPacienteResponse> agregarContacto(
            @PathVariable Long pacienteId,
            @Valid @RequestBody ContactoPacienteRequest request
    ) {
        ContactoPaciente guardado =
                contactoPacienteService.agregarContacto(pacienteId, request);

        // La URL del contacto creado será /pacientes/{pacienteId}/contactos/{id}.
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
     * PUT /pacientes/{pacienteId}/contactos/{contactoId}
     * Actualiza un contacto concreto del paciente.
     */
    @PutMapping("/{contactoId}")
    public ContactoPacienteResponse actualizarContacto(
            @PathVariable Long pacienteId,
            @PathVariable Long contactoId,
            @Valid @RequestBody ContactoPacienteRequest request
    ) {
        ContactoPaciente actualizado =
                contactoPacienteService.actualizarContacto(
                        pacienteId,
                        contactoId,
                        request
                );

        return convertirAResponse(actualizado);
    }

    /**
     * DELETE /pacientes/{pacienteId}/contactos/{contactoId}
     * Elimina un contacto y responde con HTTP 204 No Content.
     */
    @DeleteMapping("/{contactoId}")
    public ResponseEntity<Void> eliminarContacto(
            @PathVariable Long pacienteId,
            @PathVariable Long contactoId
    ) {
        contactoPacienteService.eliminarContacto(pacienteId, contactoId);

        return ResponseEntity.noContent().build();
    }

    /**
     * Convierte la entidad JPA al DTO que devolverá la API.
     */
    private ContactoPacienteResponse convertirAResponse(
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