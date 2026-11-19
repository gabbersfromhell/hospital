package com.toni.hospital.servicio;

import com.toni.hospital.dto.HabitacionRequest;
import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.modelo.Planta;
import com.toni.hospital.modelo.UnidadHospitalaria;
import com.toni.hospital.repositorio.CamaRepository;
import com.toni.hospital.repositorio.HabitacionRepository;
import com.toni.hospital.repositorio.PlantaRepository;
import com.toni.hospital.repositorio.UnidadHospitalariaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final PlantaRepository plantaRepository;
    private final UnidadHospitalariaRepository unidadHospitalariaRepository;
    private final CamaRepository camaRepository;

    public HabitacionService(
            HabitacionRepository habitacionRepository,
            PlantaRepository plantaRepository,
            UnidadHospitalariaRepository unidadHospitalariaRepository,
            CamaRepository camaRepository
    ) {
        this.habitacionRepository = habitacionRepository;
        this.plantaRepository = plantaRepository;
        this.unidadHospitalariaRepository = unidadHospitalariaRepository;
        this.camaRepository = camaRepository;
    }

    @Transactional(readOnly = true)
    public List<Habitacion> listarHabitaciones() {
        return habitacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Habitacion buscarHabitacionPorId(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Habitación no encontrada"
                ));
    }

    @Transactional
    public Habitacion guardarHabitacion(HabitacionRequest request) {
        Planta planta = buscarPlanta(request.plantaId());
        UnidadHospitalaria unidad =
                buscarUnidad(request.unidadHospitalariaId());

        comprobarMismoHospital(planta, unidad);
        comprobarNumeroDisponible(
                planta.getId(),
                request.numeroHabitacion(),
                null
        );

        Habitacion habitacion = new Habitacion();
        habitacion.setNumeroHabitacion(request.numeroHabitacion());
        habitacion.setPlanta(planta);
        habitacion.setUnidadHospitalaria(unidad);

        return habitacionRepository.save(habitacion);
    }

    @Transactional
    public Habitacion actualizarHabitacion(
            Long id,
            HabitacionRequest request
    ) {
        Habitacion habitacion = buscarHabitacionPorId(id);
        Planta nuevaPlanta = buscarPlanta(request.plantaId());
        UnidadHospitalaria nuevaUnidad =
                buscarUnidad(request.unidadHospitalariaId());

        comprobarMismoHospital(nuevaPlanta, nuevaUnidad);
        comprobarNumeroDisponible(
                nuevaPlanta.getId(),
                request.numeroHabitacion(),
                id
        );

        boolean cambiaIdentificacion =
                !habitacion.getNumeroHabitacion()
                        .equals(request.numeroHabitacion())
                        || !habitacion.getPlanta().getId()
                        .equals(nuevaPlanta.getId())
                        || !habitacion.getUnidadHospitalaria().getId()
                        .equals(nuevaUnidad.getId());

        /*
         * Evitamos cambiar la ubicación o unidad de una habitación con camas,
         * porque esas camas pueden tener historial de asignaciones.
         */
        if (cambiaIdentificacion
                && camaRepository.existsByHabitacion_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede cambiar una habitación que ya tiene camas"
            );
        }

        habitacion.setNumeroHabitacion(request.numeroHabitacion());
        habitacion.setPlanta(nuevaPlanta);
        habitacion.setUnidadHospitalaria(nuevaUnidad);

        return habitacionRepository.save(habitacion);
    }

    @Transactional
    public void eliminarHabitacion(Long id) {
        buscarHabitacionPorId(id);

        if (camaRepository.existsByHabitacion_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar una habitación que tiene camas"
            );
        }

        habitacionRepository.deleteById(id);
    }

    private Planta buscarPlanta(Long plantaId) {
        return plantaRepository.findById(plantaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Planta no encontrada"
                ));
    }

    private UnidadHospitalaria buscarUnidad(Long unidadId) {
        UnidadHospitalaria unidad = unidadHospitalariaRepository
                .findById(unidadId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unidad hospitalaria no encontrada"
                ));

        if (!unidad.isActiva()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La unidad hospitalaria está inactiva"
            );
        }

        return unidad;
    }

    private void comprobarMismoHospital(
            Planta planta,
            UnidadHospitalaria unidad
    ) {
        Long hospitalDePlanta = planta.getHospital().getId();
        Long hospitalDeUnidad = unidad.getHospital().getId();

        if (!hospitalDePlanta.equals(hospitalDeUnidad)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La planta y la unidad deben pertenecer al mismo hospital"
            );
        }
    }

    private void comprobarNumeroDisponible(
            Long plantaId,
            String numeroHabitacion,
            Long habitacionIdExcluida
    ) {
        boolean duplicado = habitacionIdExcluida == null
                ? habitacionRepository
                .existsByPlanta_IdAndNumeroHabitacion(
                        plantaId,
                        numeroHabitacion
                )
                : habitacionRepository
                .existsByPlanta_IdAndNumeroHabitacionAndIdNot(
                        plantaId,
                        numeroHabitacion,
                        habitacionIdExcluida
                );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ese número de habitación ya existe en la planta"
            );
        }
    }
}