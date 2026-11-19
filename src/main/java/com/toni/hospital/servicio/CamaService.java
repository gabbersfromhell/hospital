package com.toni.hospital.servicio;

import com.toni.hospital.dto.CamaRequest;
import com.toni.hospital.modelo.AsignacionCama;
import com.toni.hospital.modelo.Cama;
import com.toni.hospital.modelo.EstadoCama;
import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.repositorio.AsignacionCamaRepository;
import com.toni.hospital.repositorio.CamaRepository;
import com.toni.hospital.repositorio.HabitacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CamaService {

    private final CamaRepository camaRepository;
    private final HabitacionRepository habitacionRepository;
    private final AsignacionCamaRepository asignacionCamaRepository;

    public CamaService(
            CamaRepository camaRepository,
            HabitacionRepository habitacionRepository,
            AsignacionCamaRepository asignacionCamaRepository
    ) {
        this.camaRepository = camaRepository;
        this.habitacionRepository = habitacionRepository;
        this.asignacionCamaRepository = asignacionCamaRepository;
    }

    @Transactional(readOnly = true)
    public List<Cama> listarCamas() {
        return camaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cama> listarCamasDisponibles() {
        return camaRepository.findAll()
                .stream()
                .filter(cama -> cama.getEstado() == EstadoCama.OPERATIVA)
                .filter(cama -> !estaOcupada(cama.getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public Cama buscarCamaPorId(Long id) {
        return camaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cama no encontrada"
                ));
    }

    @Transactional(readOnly = true)
    public boolean estaOcupada(Long camaId) {
        return asignacionCamaRepository
                .existsByCama_IdAndFechaHoraFinIsNull(camaId);
    }

    @Transactional
    public Cama guardarCama(CamaRequest request) {
        validarNumeroCama(request.numeroCama());

        Habitacion habitacion = buscarHabitacion(request.habitacionId());

        if (camaRepository.existsByHabitacion_IdAndNumeroCama(
                habitacion.getId(),
                request.numeroCama()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe esa posición de cama en la habitación"
            );
        }

        Cama cama = new Cama();
        cama.setNumeroCama(request.numeroCama());
        cama.setHabitacion(habitacion);
        cama.setEstado(request.estado());

        return camaRepository.save(cama);
    }

    @Transactional
    public Cama actualizarCama(Long id, CamaRequest request) {
        validarNumeroCama(request.numeroCama());

        Cama cama = buscarCamaPorId(id);
        Habitacion nuevaHabitacion = buscarHabitacion(request.habitacionId());

        boolean cambiaUbicacion =
                !cama.getHabitacion().getId().equals(nuevaHabitacion.getId())
                        || !cama.getNumeroCama().equals(request.numeroCama());

        /*
         * La ubicación física de una cama no se cambia si hay asignaciones
         * históricas: estas deben seguir señalando la ubicación original.
         */
        if (cambiaUbicacion
                && asignacionCamaRepository.existsByCama_Id(cama.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede cambiar la ubicación de una cama con historial"
            );
        }

        boolean posicionOcupada =
                camaRepository.existsByHabitacion_IdAndNumeroCamaAndIdNot(
                        nuevaHabitacion.getId(),
                        request.numeroCama(),
                        id
                );

        if (posicionOcupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe esa posición de cama en la habitación"
            );
        }

        boolean ocupada = estaOcupada(id);

        if (ocupada && request.estado() != EstadoCama.OPERATIVA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede retirar de servicio una cama ocupada"
            );
        }

        cama.setNumeroCama(request.numeroCama());
        cama.setHabitacion(nuevaHabitacion);
        cama.setEstado(request.estado());

        return camaRepository.save(cama);
    }

    private Habitacion buscarHabitacion(Long habitacionId) {
        return habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Habitación no encontrada"
                ));
    }

    /**
     * El modelo de este proyecto contempla dos camas por habitación:
     * número 1 y número 2.
     */
    private void validarNumeroCama(Integer numeroCama) {
        if (numeroCama < 1 || numeroCama > 2) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El número de cama debe ser 1 o 2"
            );
        }
    }
}