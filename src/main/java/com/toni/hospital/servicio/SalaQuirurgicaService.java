package com.toni.hospital.servicio;

import com.toni.hospital.dto.SalaQuirurgicaRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.SalaQuirurgica;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.SalaQuirurgicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SalaQuirurgicaService {

    private final SalaQuirurgicaRepository salaRepository;
    private final HospitalRepository hospitalRepository;

    public SalaQuirurgicaService(
            SalaQuirurgicaRepository salaRepository,
            HospitalRepository hospitalRepository
    ) {
        this.salaRepository = salaRepository;
        this.hospitalRepository = hospitalRepository;
    }

    public List<SalaQuirurgica> listarPorHospital(Long hospitalId) {
        if (!hospitalRepository.existsById(hospitalId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Hospital no encontrado"
            );
        }
        return salaRepository.findByHospital_IdOrderByCodigoAsc(hospitalId);
    }

    public SalaQuirurgica buscarPorId(Long id) {
        return salaRepository.findWithHospitalById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala quirúrgica no encontrada"
                ));
    }

    @Transactional
    public SalaQuirurgica guardar(SalaQuirurgicaRequest request) {
        if (salaRepository.existsByHospital_IdAndCodigoIgnoreCase(
                request.hospitalId(), request.codigo()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una sala con ese código en el hospital"
            );
        }
        SalaQuirurgica sala = new SalaQuirurgica();
        aplicarDatos(sala, request);
        sala.setActiva(true);
        return salaRepository.save(sala);
    }

    @Transactional
    public SalaQuirurgica actualizar(Long id, SalaQuirurgicaRequest request) {
        SalaQuirurgica sala = buscarPorId(id);
        comprobarCodigoUnico(request.hospitalId(), request.codigo(), id);
        aplicarDatos(sala, request);
        return salaRepository.save(sala);
    }

    @Transactional
    public void desactivar(Long id) {
        SalaQuirurgica sala = buscarPorId(id);
        sala.setActiva(false);
        salaRepository.save(sala);
    }

    private void aplicarDatos(
            SalaQuirurgica sala,
            SalaQuirurgicaRequest request
    ) {
        Hospital hospital = hospitalRepository.findById(request.hospitalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Hospital no encontrado"
                ));

        comprobarCodigoUnico(
                request.hospitalId(), request.codigo(), sala.getId()
        );

        sala.setHospital(hospital);
        sala.setCodigo(request.codigo().trim());
        sala.setNombre(request.nombre().trim());
        sala.setTipo(request.tipo());
        sala.setUbicacion(request.ubicacion());
    }

    private void comprobarCodigoUnico(
            Long hospitalId, String codigo, Long idActual
    ) {
        if (idActual != null
                && salaRepository.existsByHospital_IdAndCodigoIgnoreCaseAndIdNot(
                        hospitalId, codigo, idActual
                )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una sala con ese código en el hospital"
            );
        }
    }
}
