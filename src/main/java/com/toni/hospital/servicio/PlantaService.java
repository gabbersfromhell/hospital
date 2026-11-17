package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Planta;
import com.toni.hospital.repositorio.PlantaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlantaService {

    private final PlantaRepository plantaRepository;

    public PlantaService(PlantaRepository plantaRepository) {
        this.plantaRepository = plantaRepository;
    }

    public List<Planta> listarPlantas() {
        return plantaRepository.findAll();
    }

    public Planta buscarPlantaPorId(Long id) {
        return plantaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Planta no encontrada"
                ));
    }
}