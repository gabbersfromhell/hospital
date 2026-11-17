package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Interconsulta;
import com.toni.hospital.repositorio.InterconsultaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InterconsultaService {

    private final InterconsultaRepository interconsultaRepository;

    public InterconsultaService(InterconsultaRepository interconsultaRepository) {
        this.interconsultaRepository = interconsultaRepository;
    }

    public List<Interconsulta> listarInterconsultas() {
        return interconsultaRepository.findAll();
    }

    public Interconsulta buscarInterconsultaPorId(Long id) {
        return interconsultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Interconsulta no encontrada"
                ));
    }
}