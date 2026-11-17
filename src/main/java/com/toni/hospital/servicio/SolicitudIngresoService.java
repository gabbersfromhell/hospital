package com.toni.hospital.servicio;

import com.toni.hospital.modelo.SolicitudIngreso;
import com.toni.hospital.repositorio.SolicitudIngresoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SolicitudIngresoService {

    private final SolicitudIngresoRepository solicitudIngresoRepository;

    public SolicitudIngresoService(
            SolicitudIngresoRepository solicitudIngresoRepository
    ) {
        this.solicitudIngresoRepository = solicitudIngresoRepository;
    }

    public List<SolicitudIngreso> listarSolicitudesIngreso() {
        return solicitudIngresoRepository.findAll();
    }

    public SolicitudIngreso buscarSolicitudPorId(Long id) {
        return solicitudIngresoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud de ingreso no encontrada"
                ));
    }
}