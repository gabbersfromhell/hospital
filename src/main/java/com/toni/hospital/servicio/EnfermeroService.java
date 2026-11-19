package com.toni.hospital.servicio;

import com.toni.hospital.dto.EnfermeroRequest;
import com.toni.hospital.modelo.Enfermero;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.repositorio.EnfermeroRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Gestiona los datos de Enfermería. */
@Service
public class EnfermeroService {

    private final EnfermeroRepository enfermeroRepository;
    private final HospitalRepository hospitalRepository;

    public EnfermeroService(
            EnfermeroRepository enfermeroRepository,
            HospitalRepository hospitalRepository
    ) {
        this.enfermeroRepository = enfermeroRepository;
        this.hospitalRepository = hospitalRepository;
    }

    @Transactional(readOnly = true)
    public List<Enfermero> listarEnfermeros() {
        return enfermeroRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Enfermero buscarEnfermeroPorId(Long id) {
        return enfermeroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enfermero no encontrado"
                ));
    }

    @Transactional
    public Enfermero guardarEnfermero(EnfermeroRequest request) {
        if (enfermeroRepository.existsByNumeroColegiado(
                request.numeroColegiado()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un enfermero con ese número de colegiado"
            );
        }

        Hospital hospital = buscarHospital(request.hospitalId());

        Enfermero enfermero = new Enfermero();
        enfermero.setNombre(request.nombre());
        enfermero.setApellidos(request.apellidos());
        enfermero.setNumeroColegiado(request.numeroColegiado());
        enfermero.setHospital(hospital);

        return enfermeroRepository.save(enfermero);
    }

    @Transactional
    public Enfermero actualizarEnfermero(
            Long id,
            EnfermeroRequest request
    ) {
        Enfermero enfermero = buscarEnfermeroPorId(id);

        if (enfermeroRepository.existsByNumeroColegiadoAndIdNot(
                request.numeroColegiado(),
                id
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Otro enfermero ya tiene ese número de colegiado"
            );
        }

        Hospital hospital = buscarHospital(request.hospitalId());

        enfermero.setNombre(request.nombre());
        enfermero.setApellidos(request.apellidos());
        enfermero.setNumeroColegiado(request.numeroColegiado());
        enfermero.setHospital(hospital);

        return enfermeroRepository.save(enfermero);
    }

    @Transactional
    public void desactivarEnfermero(Long id) {
        Enfermero enfermero = buscarEnfermeroPorId(id);
        enfermero.setActivo(false);
        enfermeroRepository.save(enfermero);
    }

    private Hospital buscarHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }
}