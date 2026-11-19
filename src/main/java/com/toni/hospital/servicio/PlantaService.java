package com.toni.hospital.servicio;

import com.toni.hospital.dto.PlantaRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Planta;
import com.toni.hospital.repositorio.HabitacionRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.PlantaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PlantaService {

    private final PlantaRepository plantaRepository;
    private final HospitalRepository hospitalRepository;
    private final HabitacionRepository habitacionRepository;

    public PlantaService(
            PlantaRepository plantaRepository,
            HospitalRepository hospitalRepository,
            HabitacionRepository habitacionRepository
    ) {
        this.plantaRepository = plantaRepository;
        this.hospitalRepository = hospitalRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<Planta> listarPlantas() {
        return plantaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Planta buscarPlantaPorId(Long id) {
        return plantaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Planta no encontrada"
                ));
    }

    @Transactional
    public Planta guardarPlanta(PlantaRequest request) {
        Hospital hospital = buscarHospital(request.hospitalId());

        comprobarNumeroDisponible(
                hospital.getId(),
                request.numeroPlanta(),
                null
        );

        Planta planta = new Planta();
        planta.setNumeroPlanta(request.numeroPlanta());
        planta.setHospital(hospital);

        return plantaRepository.save(planta);
    }

    @Transactional
    public Planta actualizarPlanta(Long id, PlantaRequest request) {
        Planta planta = buscarPlantaPorId(id);
        Hospital hospital = buscarHospital(request.hospitalId());

        comprobarNumeroDisponible(
                hospital.getId(),
                request.numeroPlanta(),
                id
        );

        planta.setNumeroPlanta(request.numeroPlanta());
        planta.setHospital(hospital);

        return plantaRepository.save(planta);
    }

    @Transactional
    public void eliminarPlanta(Long id) {
        buscarPlantaPorId(id);

        if (habitacionRepository.existsByPlanta_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar una planta que tiene habitaciones"
            );
        }

        plantaRepository.deleteById(id);
    }

    private Hospital buscarHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }

    private void comprobarNumeroDisponible(
            Long hospitalId,
            Integer numeroPlanta,
            Long plantaIdExcluida
    ) {
        boolean duplicado = plantaIdExcluida == null
                ? plantaRepository.existsByHospital_IdAndNumeroPlanta(
                hospitalId,
                numeroPlanta
        )
                : plantaRepository
                .existsByHospital_IdAndNumeroPlantaAndIdNot(
                        hospitalId,
                        numeroPlanta,
                        plantaIdExcluida
                );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ese número de planta ya existe en el hospital"
            );
        }
    }
}