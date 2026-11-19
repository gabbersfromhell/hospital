package com.toni.hospital.servicio;

import com.toni.hospital.dto.SolicitudPruebaDiagnosticaRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.CitaAmbulatoria;
import com.toni.hospital.modelo.EstadoSolicitudPrueba;
import com.toni.hospital.modelo.IngresoHospitalario;
import com.toni.hospital.modelo.SolicitudPruebaDiagnostica;
import com.toni.hospital.modelo.TipoProfesionalSolicitante;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.CitaAmbulatoriaRepository;
import com.toni.hospital.repositorio.IngresoHospitalarioRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import com.toni.hospital.repositorio.SolicitudPruebaDiagnosticaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/** Operaciones sobre solicitudes de pruebas diagnósticas. */
@Service
public class SolicitudPruebaDiagnosticaService {

    private final SolicitudPruebaDiagnosticaRepository solicitudRepository;
    private final ServicioHospitalarioRepository servicioRepository;
    private final AtencionUrgenciasRepository atencionRepository;
    private final IngresoHospitalarioRepository ingresoRepository;
    private final CitaAmbulatoriaRepository citaRepository;

    public SolicitudPruebaDiagnosticaService(
            SolicitudPruebaDiagnosticaRepository solicitudRepository,
            ServicioHospitalarioRepository servicioRepository,
            AtencionUrgenciasRepository atencionRepository,
            IngresoHospitalarioRepository ingresoRepository,
            CitaAmbulatoriaRepository citaRepository
    ) {
        this.solicitudRepository = solicitudRepository;
        this.servicioRepository = servicioRepository;
        this.atencionRepository = atencionRepository;
        this.ingresoRepository = ingresoRepository;
        this.citaRepository = citaRepository;
    }

    /** Lista las solicitudes de la más reciente a la más antigua. */
    public List<SolicitudPruebaDiagnostica> listarSolicitudes() {
        return solicitudRepository.findAllByOrderByFechaHoraSolicitudDesc();
    }

    /** Busca una solicitud por ID o responde con 404 si no existe. */
    public SolicitudPruebaDiagnostica buscarPorId(Long id) {
        return solicitudRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud de prueba diagnóstica no encontrada"
                ));
    }

    /**
     * Registra una solicitud asociada a urgencias, un ingreso
     * o una cita ambulatoria.
     */
    @Transactional
    public SolicitudPruebaDiagnostica guardar(
            SolicitudPruebaDiagnosticaRequest request
    ) {
        validarContextoUnico(request);
        validarDetalleOtro(request);

        ServicioHospitalario servicio = servicioRepository
                .findById(request.servicioDestinoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Servicio hospitalario no encontrado"
                ));

        if (!servicio.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El servicio destino está inactivo"
            );
        }

        SolicitudPruebaDiagnostica solicitud =
                new SolicitudPruebaDiagnostica();

        solicitud.setServicioDestino(servicio);
        solicitud.setTipo(request.tipo());
        solicitud.setNombrePrueba(request.nombrePrueba());
        solicitud.setFechaHoraSolicitud(LocalDateTime.now());
        solicitud.setIndicacionClinica(request.indicacionClinica());
        solicitud.setEstado(EstadoSolicitudPrueba.SOLICITADA);
        solicitud.setTipoProfesionalSolicitante(
                request.tipoProfesionalSolicitante()
        );

        if (request.tipoProfesionalSolicitante()
                == TipoProfesionalSolicitante.OTRO) {
            solicitud.setDetalleOtroProfesional(
                    request.detalleOtroProfesional().trim()
            );
        }

        if (request.atencionUrgenciasId() != null) {
            AtencionUrgencias atencion = atencionRepository
                    .findById(request.atencionUrgenciasId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Atención de urgencias no encontrada"
                    ));

            solicitud.setAtencionUrgencias(atencion);
        }

        if (request.ingresoHospitalarioId() != null) {
            IngresoHospitalario ingreso = ingresoRepository
                    .findById(request.ingresoHospitalarioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ingreso hospitalario no encontrado"
                    ));

            solicitud.setIngresoHospitalario(ingreso);
        }

        if (request.citaAmbulatoriaId() != null) {
            CitaAmbulatoria cita = citaRepository
                    .findById(request.citaAmbulatoriaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cita ambulatoria no encontrada"
                    ));

            solicitud.setCitaAmbulatoria(cita);
        }

        return solicitudRepository.save(solicitud);
    }

    /** Exige exactamente uno de los tres contextos posibles. */
    private void validarContextoUnico(
            SolicitudPruebaDiagnosticaRequest request
    ) {
        int cantidadContextos = 0;

        if (request.atencionUrgenciasId() != null) {
            cantidadContextos++;
        }

        if (request.ingresoHospitalarioId() != null) {
            cantidadContextos++;
        }

        if (request.citaAmbulatoriaId() != null) {
            cantidadContextos++;
        }

        if (cantidadContextos != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Debe indicarse exactamente un contexto: "
                            + "urgencias, ingreso o cita ambulatoria"
            );
        }
    }

    /** Comprueba que OTRO tenga detalle y que las demás categorías no. */
    private void validarDetalleOtro(
            SolicitudPruebaDiagnosticaRequest request
    ) {
        boolean esOtro = request.tipoProfesionalSolicitante()
                == TipoProfesionalSolicitante.OTRO;

        boolean detalleVacio = request.detalleOtroProfesional() == null
                || request.detalleOtroProfesional().isBlank();

        if (esOtro && detalleVacio) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica la profesión cuando seleccionas OTRO"
            );
        }

        if (!esOtro && !detalleVacio) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El detalle profesional solo se indica al seleccionar OTRO"
            );
        }
    }
}