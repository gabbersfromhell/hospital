package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.repositorio.HospitalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
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
}