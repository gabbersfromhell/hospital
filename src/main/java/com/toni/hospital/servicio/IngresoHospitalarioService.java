package com.toni.hospital.servicio;

import com.toni.hospital.dto.AltaHospitalariaRequest;
import com.toni.hospital.modelo.AsignacionCama;
import com.toni.hospital.modelo.EstadoSolicitudIngreso;
import com.toni.hospital.modelo.IngresoHospitalario;
import com.toni.hospital.modelo.SolicitudIngreso;
import com.toni.hospital.repositorio.AsignacionCamaRepository;
import com.toni.hospital.repositorio.IngresoHospitalarioRepository;
import com.toni.hospital.repositorio.SolicitudIngresoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IngresoHospitalarioService {

    private final IngresoHospitalarioRepository ingresoHospitalarioRepository;
    private final SolicitudIngresoRepository solicitudIngresoRepository;
    private final AsignacionCamaRepository asignacionCamaRepository;

    public IngresoHospitalarioService(
            IngresoHospitalarioRepository ingresoHospitalarioRepository,
            SolicitudIngresoRepository solicitudIngresoRepository,
            AsignacionCamaRepository asignacionCamaRepository
    ) {
        this.ingresoHospitalarioRepository = ingresoHospitalarioRepository;
        this.solicitudIngresoRepository = solicitudIngresoRepository;
        this.asignacionCamaRepository = asignacionCamaRepository;
    }

    @Transactional(readOnly = true)
    public List<IngresoHospitalario> listarIngresos() {
        return ingresoHospitalarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public IngresoHospitalario buscarIngresoPorId(Long id) {
        return ingresoHospitalarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Ingreso hospitalario no encontrado"
                ));
    }

    /**
     * Crea el ingreso efectivo a partir de una solicitud pendiente.
     */
    @Transactional
    public IngresoHospitalario crearDesdeSolicitud(Long solicitudId) {
        SolicitudIngreso solicitud = solicitudIngresoRepository
                .findById(solicitudId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud de ingreso no encontrada"
                ));

        if (solicitud.getEstado() != EstadoSolicitudIngreso.PENDIENTE) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La solicitud no está pendiente"
            );
        }

        if (ingresoHospitalarioRepository
                .findBySolicitudIngreso_Id(solicitudId)
                .isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta solicitud ya tiene un ingreso asociado"
            );
        }

        IngresoHospitalario ingreso = new IngresoHospitalario();
        ingreso.setSolicitudIngreso(solicitud);
        ingreso.setFechaHoraIngreso(LocalDateTime.now());

        IngresoHospitalario guardado =
                ingresoHospitalarioRepository.save(ingreso);

        solicitud.setEstado(EstadoSolicitudIngreso.INGRESO_REALIZADO);
        solicitudIngresoRepository.save(solicitud);

        return guardado;
    }

    /**
     * Registra el alta y cierra la asignación de cama activa, si la hay.
     */
    @Transactional
    public IngresoHospitalario registrarAlta(
            Long id,
            AltaHospitalariaRequest request
    ) {
        IngresoHospitalario ingreso = buscarIngresoPorId(id);

        if (ingreso.getFechaHoraAlta() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El ingreso ya tiene registrada el alta"
            );
        }

        LocalDateTime fechaHoraAlta = LocalDateTime.now();
        ingreso.setFechaHoraAlta(fechaHoraAlta);
        ingreso.setTipoAlta(request.tipoAlta());

        List<AsignacionCama> asignacionesActivas =
                asignacionCamaRepository
                        .findByIngresoHospitalario_IdAndFechaHoraFinIsNull(id);

        for (AsignacionCama asignacion : asignacionesActivas) {
            asignacion.setFechaHoraFin(fechaHoraAlta);
        }

        return ingresoHospitalarioRepository.save(ingreso);
    }
}