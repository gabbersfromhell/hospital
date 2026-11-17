package com.toni.hospital.servicio;

import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AtencionUrgenciasService {

    private final AtencionUrgenciasRepository atencionUrgenciasRepository;

    public AtencionUrgenciasService(
            AtencionUrgenciasRepository atencionUrgenciasRepository
    ) {
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
    }

    public List<AtencionUrgencias> listarAtenciones() {
        return atencionUrgenciasRepository.findAll();
    }

    public AtencionUrgencias buscarAtencionPorId(Long id) {
        return atencionUrgenciasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de urgencias no encontrada"
                ));
    }
}