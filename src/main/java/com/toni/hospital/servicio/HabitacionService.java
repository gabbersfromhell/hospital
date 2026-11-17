package com.toni.hospital.servicio;

import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.repositorio.HabitacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;

    public HabitacionService(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    public List<Habitacion> listarHabitaciones() {
        return habitacionRepository.findAll();
    }

    public Habitacion buscarHabitacionPorId(Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Habitación no encontrada"
                ));
    }
}