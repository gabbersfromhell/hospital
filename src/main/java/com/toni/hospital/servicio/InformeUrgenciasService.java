package com.toni.hospital.servicio;

import com.toni.hospital.modelo.InformeUrgencias;
import com.toni.hospital.repositorio.InformeUrgenciasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class InformeUrgenciasService {

    private final InformeUrgenciasRepository informeUrgenciasRepository;

    public InformeUrgenciasService(
            InformeUrgenciasRepository informeUrgenciasRepository
    ) {
        this.informeUrgenciasRepository = informeUrgenciasRepository;
    }

    public List<InformeUrgencias> listarInformes() {
        return informeUrgenciasRepository.findAll();
    }

    public InformeUrgencias buscarInformePorId(Long id) {
        return informeUrgenciasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Informe de urgencias no encontrado"
                ));
    }
}