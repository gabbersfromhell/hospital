package com.toni.hospital.servicio;

import com.toni.hospital.dto.SolicitudIngresoRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.EstadoSolicitudIngreso;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.modelo.SolicitudIngreso;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.MedicoRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import com.toni.hospital.repositorio.SolicitudIngresoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestiona solicitudes médicas de ingreso desde Urgencias.
 */
@Service
public class SolicitudIngresoService {

    private final SolicitudIngresoRepository solicitudIngresoRepository;
    private final AtencionUrgenciasRepository atencionUrgenciasRepository;
    private final MedicoRepository medicoRepository;
    private final ServicioHospitalarioRepository servicioHospitalarioRepository;

    public SolicitudIngresoService(
            SolicitudIngresoRepository solicitudIngresoRepository,
            AtencionUrgenciasRepository atencionUrgenciasRepository,
            MedicoRepository medicoRepository,
            ServicioHospitalarioRepository servicioHospitalarioRepository
    ) {
        this.solicitudIngresoRepository = solicitudIngresoRepository;
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
        this.medicoRepository = medicoRepository;
        this.servicioHospitalarioRepository = servicioHospitalarioRepository;
    }

    /**
     * Devuelve las solicitudes asociadas a una atención.
     */
    @Transactional(readOnly = true)
    public List<SolicitudIngreso> listarPorAtencion(Long atencionId) {
        comprobarAtencionExiste(atencionId);

        return solicitudIngresoRepository
                .findByAtencionUrgencias_IdOrderByFechaSolicitudDesc(
                        atencionId
                );
    }

    /**
     * Busca una solicitud y comprueba que pertenezca a la atención indicada.
     */
    @Transactional(readOnly = true)
    public SolicitudIngreso buscarPorAtencion(Long atencionId, Long id) {
        comprobarAtencionExiste(atencionId);

        SolicitudIngreso solicitud = buscarSolicitudPorId(id);

        if (!solicitud.getAtencionUrgencias().getId().equals(atencionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Solicitud no encontrada para esa atención"
            );
        }

        return solicitud;
    }

    /**
     * Registra la solicitud. Todavía no crea un ingreso hospitalario.
     */
    @Transactional
    public SolicitudIngreso crearSolicitud(
            Long atencionId,
            SolicitudIngresoRequest request
    ) {
        AtencionUrgencias atencion = comprobarAtencionExiste(atencionId);

        Medico medico = medicoRepository
                .findById(request.medicoSolicitanteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico solicitante no encontrado"
                ));

        ServicioHospitalario servicio = servicioHospitalarioRepository
                .findById(request.servicioHospitalarioId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Servicio hospitalario no encontrado"
                ));

        SolicitudIngreso solicitud = new SolicitudIngreso();
        solicitud.setAtencionUrgencias(atencion);
        solicitud.setMedicoSolicitante(medico);
        solicitud.setServicioHospitalario(servicio);
        solicitud.setFechaSolicitud(LocalDateTime.now());
        solicitud.setMotivo(request.motivo());
        solicitud.setEstado(EstadoSolicitudIngreso.PENDIENTE);

        atencion.agregarSolicitudIngreso(solicitud);

        return solicitudIngresoRepository.save(solicitud);
    }

    private SolicitudIngreso buscarSolicitudPorId(Long id) {
        return solicitudIngresoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud de ingreso no encontrada"
                ));
    }

    private AtencionUrgencias comprobarAtencionExiste(Long atencionId) {
        return atencionUrgenciasRepository.findById(atencionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de urgencias no encontrada"
                ));
    }
}