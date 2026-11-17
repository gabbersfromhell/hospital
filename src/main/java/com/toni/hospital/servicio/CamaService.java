package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Cama;
import com.toni.hospital.repositorio.CamaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CamaService {

    private final CamaRepository camaRepository;

    public CamaService(CamaRepository camaRepository) {
        this.camaRepository = camaRepository;
    }

    public List<Cama> listarCamas() {
        return camaRepository.findAll();
    }

    public Cama buscarCamaPorId(Long id) {
        return camaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cama no encontrada"
                ));
    }
}