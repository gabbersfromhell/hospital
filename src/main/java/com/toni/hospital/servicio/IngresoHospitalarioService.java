package com.toni.hospital.servicio;

import com.toni.hospital.modelo.IngresoHospitalario;
import com.toni.hospital.repositorio.IngresoHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IngresoHospitalarioService {

    private final IngresoHospitalarioRepository ingresoHospitalarioRepository;

    public IngresoHospitalarioService(
            IngresoHospitalarioRepository ingresoHospitalarioRepository
    ) {
        this.ingresoHospitalarioRepository = ingresoHospitalarioRepository;
    }

    public List<IngresoHospitalario> listarIngresos() {
        return ingresoHospitalarioRepository.findAll();
    }

    public IngresoHospitalario buscarIngresoPorId(Long id) {
        return ingresoHospitalarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ingreso hospitalario no encontrado"
                ));
    }
}