package com.toni.hospital.servicio;

import com.toni.hospital.modelo.UnidadHospitalaria;
import com.toni.hospital.repositorio.UnidadHospitalariaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UnidadHospitalariaService {

    private final UnidadHospitalariaRepository unidadHospitalariaRepository;

    public UnidadHospitalariaService(
            UnidadHospitalariaRepository unidadHospitalariaRepository
    ) {
        this.unidadHospitalariaRepository = unidadHospitalariaRepository;
    }

    public List<UnidadHospitalaria> listarUnidades() {
        return unidadHospitalariaRepository.findAll();
    }

    public UnidadHospitalaria buscarUnidadPorId(Long id) {
        return unidadHospitalariaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unidad hospitalaria no encontrada"
                ));
    }
}