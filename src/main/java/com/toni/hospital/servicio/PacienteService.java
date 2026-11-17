package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.TipoDocumento;
import com.toni.hospital.repositorio.PacienteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAll();
    }

    public Paciente buscarPacientePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));
    }

    public Paciente buscarPorDocumento(
            TipoDocumento tipoDocumento,
            String numeroDocumento
    ) {
        return pacienteRepository
                .findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));
    }
}