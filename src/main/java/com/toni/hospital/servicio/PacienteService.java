package com.toni.hospital.servicio;

import com.toni.hospital.dto.ContactoPacienteRequest;
import com.toni.hospital.dto.PacienteRequest;
import com.toni.hospital.modelo.ContactoPaciente;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.Poblacion;
import com.toni.hospital.repositorio.PacienteRepository;
import com.toni.hospital.repositorio.PoblacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

/**
 * Contiene la lógica de negocio relacionada con los pacientes.
 */
@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PoblacionRepository poblacionRepository;

    public PacienteService(
            PacienteRepository pacienteRepository,
            PoblacionRepository poblacionRepository
    ) {
        this.pacienteRepository = pacienteRepository;
        this.poblacionRepository = poblacionRepository;
    }

    /**
     * Devuelve todos los pacientes.
     */
    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    /**
     * Busca un paciente por su ID.
     *
     * Si no existe, responde con HTTP 404.
     */
    @Transactional(readOnly = true)
    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));
    }

    /**
     * Crea un paciente nuevo.
     */
    @Transactional
    public Paciente guardarPaciente(PacienteRequest request) {
        comprobarDocumentoDisponible(request, null);

        Paciente paciente = new Paciente();

        copiarDatos(request, paciente);
        actualizarPoblacion(request, paciente);
        reemplazarContactos(request, paciente);

        /*
         * Los contactos se guardan junto con el paciente gracias
         * al cascade configurado en la relación OneToMany.
         */
        return pacienteRepository.save(paciente);
    }

    /**
     * Actualiza un paciente existente.
     */
    @Transactional
    public Paciente actualizarPaciente(Long id, PacienteRequest request) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));

        /*
         * Se excluye el propio paciente de la comprobación:
         * puede conservar su documento al actualizar otros datos.
         */
        comprobarDocumentoDisponible(request, id);

        copiarDatos(request, paciente);
        actualizarPoblacion(request, paciente);
        reemplazarContactos(request, paciente);

        return pacienteRepository.save(paciente);
    }

    /**
     * Comprueba que el tipo y número de documento no estén asociados
     * a otro paciente.
     *
     * idPacienteExcluido es null al crear. Al actualizar, contiene el ID
     * del paciente que estamos modificando.
     */
    private void comprobarDocumentoDisponible(
            PacienteRequest request,
            Long idPacienteExcluido
    ) {
        boolean documentoDuplicado = pacienteRepository
                .findByTipoDocumentoAndNumeroDocumento(
                        request.tipoDocumento(),
                        request.numeroDocumento()
                )
                .filter(pacienteEncontrado ->
                        idPacienteExcluido == null
                                || !Objects.equals(
                                pacienteEncontrado.getId(),
                                idPacienteExcluido
                        )
                )
                .isPresent();

        if (documentoDuplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe otro paciente con ese documento"
            );
        }
    }

    /**
     * Copia los datos simples del DTO a la entidad.
     */
    private void copiarDatos(PacienteRequest request, Paciente paciente) {
        paciente.setNombre(request.nombre());
        paciente.setApellidos(request.apellidos());
        paciente.setTipoDocumento(request.tipoDocumento());
        paciente.setNumeroDocumento(request.numeroDocumento());
        paciente.setCip(request.cip());
        paciente.setNumeroSeguridadSocial(
                request.numeroSeguridadSocial()
        );
        paciente.setDireccion(request.direccion());
        paciente.setCorreoElectronico(request.correoElectronico());
    }

    /**
     * Busca la población por su ID y la asocia al paciente.
     *
     * Si el ID recibido es null, el paciente queda sin población asociada.
     */
    private void actualizarPoblacion(
            PacienteRequest request,
            Paciente paciente
    ) {
        if (request.poblacionId() == null) {
            paciente.setPoblacion(null);
            return;
        }

        Poblacion poblacion = poblacionRepository
                .findById(request.poblacionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Población no encontrada"
                ));

        paciente.setPoblacion(poblacion);
    }

    /**
     * Reemplaza la lista actual de contactos por la recibida en el request.
     *
     * Al quitar los contactos anteriores, orphanRemoval permite que JPA
     * los elimine de la base de datos al guardar la actualización.
     */
    private void reemplazarContactos(
            PacienteRequest request,
            Paciente paciente
    ) {
        // Copiamos la lista para poder quitar sus elementos mientras la recorremos.
        for (ContactoPaciente contactoAnterior :
                List.copyOf(paciente.getContactos())) {
            paciente.quitarContacto(contactoAnterior);
        }

        // Si no se reciben contactos, el paciente queda sin contactos asociados.
        if (request.contactos() == null) {
            return;
        }

        for (ContactoPacienteRequest contactoRequest : request.contactos()) {
            ContactoPaciente contacto = new ContactoPaciente();

            contacto.setNombre(contactoRequest.nombre());
            contacto.setApellidos(contactoRequest.apellidos());
            contacto.setRelacionConPaciente(
                    contactoRequest.relacionConPaciente()
            );
            contacto.setTelefono(contactoRequest.telefono());
            contacto.setContactoPrincipal(
                    contactoRequest.contactoPrincipal()
            );

            // También establece el vínculo desde ContactoPaciente hacia Paciente.
            paciente.agregarContacto(contacto);
        }
    }
}