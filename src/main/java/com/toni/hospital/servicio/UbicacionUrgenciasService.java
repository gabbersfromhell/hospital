package com.toni.hospital.servicio;

import com.toni.hospital.modelo.UbicacionUrgencias;
import com.toni.hospital.repositorio.UbicacionUrgenciasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UbicacionUrgenciasService {

    private final UbicacionUrgenciasRepository ubicacionUrgenciasRepository;

    public UbicacionUrgenciasService(
            UbicacionUrgenciasRepository ubicacionUrgenciasRepository
    ) {
        this.ubicacionUrgenciasRepository = ubicacionUrgenciasRepository;
    }

    public List<UbicacionUrgencias> listarUbicaciones() {
        return ubicacionUrgenciasRepository.findAll();
    }

    public UbicacionUrgencias buscarUbicacionPorId(Long id) {
        return ubicacionUrgenciasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ubicación de urgencias no encontrada"
                ));
    }
}