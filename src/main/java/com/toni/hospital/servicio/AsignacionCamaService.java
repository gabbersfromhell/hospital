package com.toni.hospital.servicio;

import com.toni.hospital.dto.AsignacionCamaRequest;
import com.toni.hospital.modelo.AsignacionCama;
import com.toni.hospital.modelo.Cama;
import com.toni.hospital.modelo.EstadoCama;
import com.toni.hospital.modelo.IngresoHospitalario;
import com.toni.hospital.repositorio.AsignacionCamaRepository;
import com.toni.hospital.repositorio.CamaRepository;
import com.toni.hospital.repositorio.IngresoHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsignacionCamaService {

    private final AsignacionCamaRepository asignacionCamaRepository;
    private final IngresoHospitalarioRepository ingresoHospitalarioRepository;
    private final CamaRepository camaRepository;

    public AsignacionCamaService(
            AsignacionCamaRepository asignacionCamaRepository,
            IngresoHospitalarioRepository ingresoHospitalarioRepository,
            CamaRepository camaRepository
    ) {
        this.asignacionCamaRepository = asignacionCamaRepository;
        this.ingresoHospitalarioRepository = ingresoHospitalarioRepository;
        this.camaRepository = camaRepository;
    }

    /** Devuelve el historial de camas de un ingreso. */
    @Transactional(readOnly = true)
    public List<AsignacionCama> listarPorIngreso(Long ingresoId) {
        buscarIngreso(ingresoId);

        return asignacionCamaRepository
                .findByIngresoHospitalario_IdOrderByFechaHoraInicioAsc(
                        ingresoId
                );
    }

    /**
     * Busca una asignación y comprueba que pertenece al ingreso indicado.
     * Si no pertenece, responde como si no existiera dentro de ese ingreso.
     */
    @Transactional(readOnly = true)
    public AsignacionCama buscarPorIngreso(
            Long ingresoId,
            Long asignacionId
    ) {
        buscarIngreso(ingresoId);

        AsignacionCama asignacion =
                asignacionCamaRepository.findById(asignacionId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Asignación de cama no encontrada"
                        ));

        if (!asignacion.getIngresoHospitalario()
                .getId()
                .equals(ingresoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Asignación de cama no encontrada para ese ingreso"
            );
        }

        return asignacion;
    }

    /**
     * Asigna una cama al ingreso.
     *
     * Si el paciente ya tenía otra cama asignada, se cierra esa asignación
     * y se crea una nueva para conservar el historial de ubicaciones.
     */
    @Transactional
    public AsignacionCama asignarCama(
            Long ingresoId,
            AsignacionCamaRequest request
    ) {
        IngresoHospitalario ingreso = buscarIngreso(ingresoId);

        if (ingreso.getFechaHoraAlta() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede asignar una cama a un ingreso dado de alta"
            );
        }

        Cama camaNueva = camaRepository.findById(request.camaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cama no encontrada"
                ));

        if (camaNueva.getEstado() != EstadoCama.OPERATIVA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La cama no está operativa"
            );
        }

        List<AsignacionCama> asignacionesActuales =
                asignacionCamaRepository
                        .findByIngresoHospitalario_IdAndFechaHoraFinIsNull(
                                ingresoId
                        );

        // Evita crear una asignación duplicada para la cama actual.
        for (AsignacionCama actual : asignacionesActuales) {
            if (actual.getCama().getId().equals(camaNueva.getId())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "El ingreso ya tiene asignada esa cama"
                );
            }
        }

        // Una cama no puede tener dos asignaciones activas a la vez.
        boolean camaOcupada = asignacionCamaRepository
                .existsByCama_IdAndFechaHoraFinIsNull(camaNueva.getId());

        if (camaOcupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La cama ya está asignada a otro ingreso"
            );
        }

        LocalDateTime ahora = LocalDateTime.now();

        // Finaliza la asignación previa si el paciente cambia de cama.
        for (AsignacionCama actual : asignacionesActuales) {
            actual.setFechaHoraFin(ahora);
        }

        AsignacionCama nueva = new AsignacionCama();
        nueva.setIngresoHospitalario(ingreso);
        nueva.setCama(camaNueva);
        nueva.setFechaHoraInicio(ahora);
        nueva.setMotivo(request.motivo());

        // Mantiene también actualizada la lista del ingreso en memoria.
        ingreso.agregarAsignacionCama(nueva);

        return asignacionCamaRepository.save(nueva);
    }

    /** Busca un ingreso o devuelve HTTP 404. */
    private IngresoHospitalario buscarIngreso(Long ingresoId) {
        return ingresoHospitalarioRepository.findById(ingresoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ingreso hospitalario no encontrado"
                ));
    }
}