package com.toni.hospital.servicio;

import com.toni.hospital.modelo.AsignacionCama;
import com.toni.hospital.repositorio.AsignacionCamaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AsignacionCamaService {

    private final AsignacionCamaRepository asignacionCamaRepository;

    public AsignacionCamaService(
            AsignacionCamaRepository asignacionCamaRepository
    ) {
        this.asignacionCamaRepository = asignacionCamaRepository;
    }

    public List<AsignacionCama> listarAsignaciones() {
        return asignacionCamaRepository.findAll();
    }

    public AsignacionCama buscarAsignacionPorId(Long id) {
        return asignacionCamaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Asignación de cama no encontrada"
                ));
    }
}