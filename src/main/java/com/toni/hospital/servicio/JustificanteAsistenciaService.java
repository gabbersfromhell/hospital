package com.toni.hospital.servicio;

import com.toni.hospital.modelo.JustificanteAsistencia;
import com.toni.hospital.repositorio.JustificanteAsistenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class JustificanteAsistenciaService {

    private final JustificanteAsistenciaRepository justificanteAsistenciaRepository;

    public JustificanteAsistenciaService(
            JustificanteAsistenciaRepository justificanteAsistenciaRepository
    ) {
        this.justificanteAsistenciaRepository = justificanteAsistenciaRepository;
    }

    public List<JustificanteAsistencia> listarJustificantes() {
        return justificanteAsistenciaRepository.findAll();
    }

    public JustificanteAsistencia buscarJustificantePorId(Long id) {
        return justificanteAsistenciaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Justificante de asistencia no encontrado"
                ));
    }
}