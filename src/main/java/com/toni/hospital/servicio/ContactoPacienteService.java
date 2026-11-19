package com.toni.hospital.servicio;

import com.toni.hospital.dto.ContactoPacienteRequest;
import com.toni.hospital.modelo.ContactoPaciente;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.repositorio.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Gestiona los contactos asociados a un paciente.
 */
@Service
public class ContactoPacienteService {

    private final PacienteRepository pacienteRepository;

    public ContactoPacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    /**
     * Devuelve los contactos de un paciente.
     */
    @Transactional(readOnly = true)
    public List<ContactoPaciente> listarContactos(Long pacienteId) {
        Paciente paciente = buscarPaciente(pacienteId);
        return paciente.getContactos();
    }

    /**
     * Añade un contacto nuevo al paciente.
     */
    @Transactional
    public ContactoPaciente agregarContacto(
            Long pacienteId,
            ContactoPacienteRequest request
    ) {
        Paciente paciente = buscarPaciente(pacienteId);

        ContactoPaciente contacto = new ContactoPaciente();
        copiarDatos(request, contacto);

        /*
         * Este método añade el contacto a la lista del paciente
         * y establece la relación desde ambos lados.
         */
        paciente.agregarContacto(contacto);

        /*
         * La relación tiene cascade, por lo que guardar el paciente
         * también guarda el nuevo contacto.
         */
        pacienteRepository.save(paciente);

        return contacto;
    }

    /**
     * Actualiza un contacto que debe pertenecer al paciente indicado.
     */
    @Transactional
    public ContactoPaciente actualizarContacto(
            Long pacienteId,
            Long contactoId,
            ContactoPacienteRequest request
    ) {
        Paciente paciente = buscarPaciente(pacienteId);
        ContactoPaciente contacto = buscarContacto(paciente, contactoId);

        copiarDatos(request, contacto);

        return contacto;
    }

    /**
     * Elimina un contacto de la lista del paciente.
     */
    @Transactional
    public void eliminarContacto(Long pacienteId, Long contactoId) {
        Paciente paciente = buscarPaciente(pacienteId);
        ContactoPaciente contacto = buscarContacto(paciente, contactoId);

        /*
         * quitarContacto actualiza la relación.
         * Con orphanRemoval, JPA elimina ese contacto de la base de datos
         * al finalizar la transacción.
         */
        paciente.quitarContacto(contacto);
    }

    /**
     * Busca al paciente o responde con HTTP 404.
     */
    private Paciente buscarPaciente(Long pacienteId) {
        return pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));
    }

    /**
     * Busca el contacto dentro de la lista de ese paciente.
     * También impide modificar un contacto perteneciente a otra persona.
     */
    private ContactoPaciente buscarContacto(
            Paciente paciente,
            Long contactoId
    ) {
        return paciente.getContactos()
                .stream()
                .filter(contacto -> contacto.getId().equals(contactoId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contacto no encontrado para ese paciente"
                ));
    }

    /**
     * Copia los campos del request a la entidad de contacto.
     */
    private void copiarDatos(
            ContactoPacienteRequest request,
            ContactoPaciente contacto
    ) {
        contacto.setNombre(request.nombre());
        contacto.setApellidos(request.apellidos());
        contacto.setRelacionConPaciente(
                request.relacionConPaciente()
        );
        contacto.setTelefono(request.telefono());
        contacto.setContactoPrincipal(
                request.contactoPrincipal()
        );
    }
}