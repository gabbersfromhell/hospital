package com.toni.hospital.servicio;

import com.toni.hospital.modelo.ContactoPaciente;
import com.toni.hospital.repositorio.ContactoPacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ContactoPacienteService {

    private final ContactoPacienteRepository contactoPacienteRepository;

    public ContactoPacienteService(
            ContactoPacienteRepository contactoPacienteRepository
    ) {
        this.contactoPacienteRepository = contactoPacienteRepository;
    }

    public List<ContactoPaciente> listarContactos() {
        return contactoPacienteRepository.findAll();
    }

    public ContactoPaciente buscarContactoPorId(Long id) {
        return contactoPacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Contacto del paciente no encontrado"
                ));
    }

    public List<ContactoPaciente> listarContactosDePaciente(Long pacienteId) {
        return contactoPacienteRepository.findByPacienteId(pacienteId);
    }
}