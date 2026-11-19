package com.toni.hospital.servicio;

import com.toni.hospital.dto.RestriccionConfidencialidadRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.RestriccionConfidencialidad;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.PacienteRepository;
import com.toni.hospital.repositorio.RestriccionConfidencialidadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/** Gestiona los periodos de confidencialidad de un paciente. */
@Service
public class RestriccionConfidencialidadService {

    private final RestriccionConfidencialidadRepository
            restriccionRepository;
    private final PacienteRepository pacienteRepository;
    private final HospitalRepository hospitalRepository;

    public RestriccionConfidencialidadService(
            RestriccionConfidencialidadRepository restriccionRepository,
            PacienteRepository pacienteRepository,
            HospitalRepository hospitalRepository
    ) {
        this.restriccionRepository = restriccionRepository;
        this.pacienteRepository = pacienteRepository;
        this.hospitalRepository = hospitalRepository;
    }

    /** Activa una restricción para un paciente en un hospital. */
    @Transactional
    public RestriccionConfidencialidad activar(
            RestriccionConfidencialidadRequest request
    ) {
        boolean yaActiva = restriccionRepository
                .existsByPaciente_IdAndHospital_IdAndFechaHoraFinIsNull(
                        request.pacienteId(),
                        request.hospitalId()
                );

        if (yaActiva) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una restricción activa para ese paciente"
            );
        }

        Paciente paciente = pacienteRepository
                .findById(request.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));

        Hospital hospital = hospitalRepository
                .findById(request.hospitalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));

        RestriccionConfidencialidad restriccion =
                new RestriccionConfidencialidad();

        restriccion.setPaciente(paciente);
        restriccion.setHospital(hospital);
        restriccion.setMotivo(request.motivo());
        restriccion.setFechaHoraInicio(LocalDateTime.now());

        return restriccionRepository.save(restriccion);
    }

    /** Finaliza una restricción que todavía esté activa. */
    @Transactional
    public RestriccionConfidencialidad finalizar(Long id) {
        RestriccionConfidencialidad restriccion =
                restriccionRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Restricción de confidencialidad no encontrada"
                        ));

        if (restriccion.getFechaHoraFin() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La restricción ya está finalizada"
            );
        }

        restriccion.setFechaHoraFin(LocalDateTime.now());

        return restriccionRepository.save(restriccion);
    }

    /** Consulta una restricción por su ID. */
    @Transactional(readOnly = true)
    public RestriccionConfidencialidad buscarPorId(Long id) {
        return restriccionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Restricción de confidencialidad no encontrada"
                ));
    }
}