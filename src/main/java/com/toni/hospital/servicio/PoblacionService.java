package com.toni.hospital.servicio;

import com.toni.hospital.dto.PoblacionRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Poblacion;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.PacienteRepository;
import com.toni.hospital.repositorio.PoblacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PoblacionService {

    private final PoblacionRepository poblacionRepository;
    private final HospitalRepository hospitalRepository;
    private final PacienteRepository pacienteRepository;

    public PoblacionService(
            PoblacionRepository poblacionRepository,
            HospitalRepository hospitalRepository,
            PacienteRepository pacienteRepository
    ) {
        this.poblacionRepository = poblacionRepository;
        this.hospitalRepository = hospitalRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public List<Poblacion> listarPoblaciones() {
        return poblacionRepository.findAll();
    }

    public Poblacion buscarPoblacionPorId(Long id) {
        return poblacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Población no encontrada"
                ));
    }

    @Transactional
    public Poblacion guardarPoblacion(PoblacionRequest request) {
        Poblacion poblacion = new Poblacion();
        poblacion.setNombre(request.nombre());
        poblacion.setHospitalReferencia(
                buscarHospitalReferencia(request.hospitalReferenciaId())
        );

        return poblacionRepository.save(poblacion);
    }

    @Transactional
    public Poblacion actualizarPoblacion(Long id, PoblacionRequest request) {
        Poblacion poblacion = buscarPoblacionPorId(id);

        poblacion.setNombre(request.nombre());
        poblacion.setHospitalReferencia(
                buscarHospitalReferencia(request.hospitalReferenciaId())
        );

        return poblacionRepository.save(poblacion);
    }

    @Transactional
    public void eliminarPoblacion(Long id) {
        buscarPoblacionPorId(id);

        if (pacienteRepository.existsByPoblacion_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar: hay pacientes asociados a esta población"
            );
        }

        poblacionRepository.deleteById(id);
    }

    private Hospital buscarHospitalReferencia(Long hospitalId) {
        if (hospitalId == null) {
            return null;
        }

        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital de referencia no encontrado"
                ));
    }
}