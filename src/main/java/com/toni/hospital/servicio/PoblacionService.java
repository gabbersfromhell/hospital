package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Poblacion;
import com.toni.hospital.repositorio.PoblacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PoblacionService {

    private final PoblacionRepository poblacionRepository;

    public PoblacionService(PoblacionRepository poblacionRepository) {
        this.poblacionRepository = poblacionRepository;
    }

    public List<Poblacion> listarPoblaciones() {
        return poblacionRepository.findAll();
    }

    public Poblacion buscarPoblacionPorId(Long id) {
        return poblacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Población no encontrada"
                ));
    }
}