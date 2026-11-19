package com.toni.hospital.servicio;

import com.toni.hospital.dto.HospitalRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final ServicioHospitalarioRepository servicioHospitalarioRepository;

    public HospitalService(
            HospitalRepository hospitalRepository,
            ServicioHospitalarioRepository servicioHospitalarioRepository
    ) {
        this.hospitalRepository = hospitalRepository;
        this.servicioHospitalarioRepository = servicioHospitalarioRepository;
    }

    public List<Hospital> listarHospitales() {
        return hospitalRepository.findAll();
    }

    public Hospital buscarHospitalPorId(Long id) {
        return hospitalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }

    @Transactional
    public Hospital guardarHospital(HospitalRequest request) {
        Hospital hospital = new Hospital();

        hospital.setNombre(request.nombre());
        hospital.setMunicipio(request.municipio());
        hospital.setDireccion(request.direccion());
        hospital.setTelefono(request.telefono());

        if (request.serviciosIds() != null) {
            for (Long servicioId : request.serviciosIds()) {
                ServicioHospitalario servicio =
                        servicioHospitalarioRepository.findById(servicioId)
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Servicio hospitalario no encontrado: " + servicioId
                                ));

                if (!servicio.isActivo()) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "No se puede asociar un servicio inactivo: "
                                    + servicio.getNombre()
                    );
                }

                hospital.agregarServicio(servicio);
            }
        }

        return hospitalRepository.save(hospital);
    }

    @Transactional
    public Hospital actualizarHospital(Long id, HospitalRequest request) {
        Hospital hospital = buscarHospitalPorId(id);

        hospital.setNombre(request.nombre());
        hospital.setMunicipio(request.municipio());
        hospital.setDireccion(request.direccion());
        hospital.setTelefono(request.telefono());

        // El PUT sustituye los servicios asociados.
        hospital.getServicios().clear();

        if (request.serviciosIds() != null) {
            for (Long servicioId : request.serviciosIds()) {
                ServicioHospitalario servicio =
                        servicioHospitalarioRepository.findById(servicioId)
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Servicio hospitalario no encontrado: " + servicioId
                                ));

                if (!servicio.isActivo()) {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "No se puede asociar un servicio inactivo: "
                                    + servicio.getNombre()
                    );
                }

                hospital.agregarServicio(servicio);
            }
        }

        return hospitalRepository.save(hospital);
    }
}