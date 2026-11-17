package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Medico;
import com.toni.hospital.repositorio.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Medico buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico no encontrado"
                ));
    }
}