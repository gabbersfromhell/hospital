package com.toni.hospital.servicio;

import com.toni.hospital.modelo.TrasladoHospitalario;
import com.toni.hospital.repositorio.TrasladoHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TrasladoHospitalarioService {

    private final TrasladoHospitalarioRepository trasladoHospitalarioRepository;

    public TrasladoHospitalarioService(
            TrasladoHospitalarioRepository trasladoHospitalarioRepository
    ) {
        this.trasladoHospitalarioRepository = trasladoHospitalarioRepository;
    }

    public List<TrasladoHospitalario> listarTraslados() {
        return trasladoHospitalarioRepository.findAll();
    }

    public TrasladoHospitalario buscarTrasladoPorId(Long id) {
        return trasladoHospitalarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Traslado hospitalario no encontrado"
                ));
    }
}