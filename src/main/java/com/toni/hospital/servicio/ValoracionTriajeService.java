package com.toni.hospital.servicio;

import com.toni.hospital.modelo.ValoracionTriaje;
import com.toni.hospital.repositorio.ValoracionTriajeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ValoracionTriajeService {

    private final ValoracionTriajeRepository valoracionTriajeRepository;

    public ValoracionTriajeService(
            ValoracionTriajeRepository valoracionTriajeRepository
    ) {
        this.valoracionTriajeRepository = valoracionTriajeRepository;
    }

    public List<ValoracionTriaje> listarValoraciones() {
        return valoracionTriajeRepository.findAll();
    }

    public ValoracionTriaje buscarValoracionPorId(Long id) {
        return valoracionTriajeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Valoración de triaje no encontrada"
                ));
    }
}