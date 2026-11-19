package com.toni.hospital.servicio;

import com.toni.hospital.dto.UbicacionUrgenciasRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.UbicacionUrgencias;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.UbicacionUrgenciasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UbicacionUrgenciasService {

    private final UbicacionUrgenciasRepository ubicacionRepository;
    private final HospitalRepository hospitalRepository;
    private final AtencionUrgenciasRepository atencionRepository;

    public UbicacionUrgenciasService(
            UbicacionUrgenciasRepository ubicacionRepository,
            HospitalRepository hospitalRepository,
            AtencionUrgenciasRepository atencionRepository
    ) {
        this.ubicacionRepository = ubicacionRepository;
        this.hospitalRepository = hospitalRepository;
        this.atencionRepository = atencionRepository;
    }

    @Transactional(readOnly = true)
    public List<UbicacionUrgencias> listarUbicaciones() {
        return ubicacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<UbicacionUrgencias> listarUbicacionesActivas() {
        return ubicacionRepository.findByActivaTrueOrderByCodigoAsc();
    }

    @Transactional(readOnly = true)
    public UbicacionUrgencias buscarUbicacionPorId(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ubicación de Urgencias no encontrada"
                ));
    }

    @Transactional
    public UbicacionUrgencias guardarUbicacion(
            UbicacionUrgenciasRequest request
    ) {
        Hospital hospital = buscarHospital(request.hospitalId());

        comprobarCodigoDisponible(
                hospital.getId(),
                request.codigo(),
                null
        );

        UbicacionUrgencias ubicacion = new UbicacionUrgencias();
        ubicacion.setCodigo(request.codigo());
        ubicacion.setHospital(hospital);
        ubicacion.setTipo(request.tipo());
        ubicacion.setActiva(request.activa());

        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public UbicacionUrgencias actualizarUbicacion(
            Long id,
            UbicacionUrgenciasRequest request
    ) {
        UbicacionUrgencias ubicacion = buscarUbicacionPorId(id);
        Hospital hospital = buscarHospital(request.hospitalId());

        comprobarCodigoDisponible(
                hospital.getId(),
                request.codigo(),
                id
        );

        if (!request.activa()
                && ubicacion.isActiva()
                && atencionRepository
                .existsByUbicacionActual_IdAndFechaHoraFinUrgenciasIsNull(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede desactivar: hay pacientes activos en esta ubicación"
            );
        }

        ubicacion.setCodigo(request.codigo());
        ubicacion.setHospital(hospital);
        ubicacion.setTipo(request.tipo());
        ubicacion.setActiva(request.activa());

        return ubicacionRepository.save(ubicacion);
    }

    @Transactional
    public void desactivarUbicacion(Long id) {
        UbicacionUrgencias ubicacion = buscarUbicacionPorId(id);

        if (atencionRepository
                .existsByUbicacionActual_IdAndFechaHoraFinUrgenciasIsNull(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede desactivar: hay pacientes activos en esta ubicación"
            );
        }

        ubicacion.setActiva(false);
        ubicacionRepository.save(ubicacion);
    }

    private Hospital buscarHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }

    private void comprobarCodigoDisponible(
            Long hospitalId,
            String codigo,
            Long ubicacionIdExcluida
    ) {
        boolean duplicado = ubicacionIdExcluida == null
                ? ubicacionRepository.existsByHospital_IdAndCodigo(
                hospitalId,
                codigo
        )
                : ubicacionRepository
                .existsByHospital_IdAndCodigoAndIdNot(
                        hospitalId,
                        codigo,
                        ubicacionIdExcluida
                );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ese código ya existe en el hospital"
            );
        }
    }
}