package com.toni.hospital.servicio;

import com.toni.hospital.dto.InformeRadiologicoRequest;
import com.toni.hospital.modelo.EstadoInformeRadiologico;
import com.toni.hospital.modelo.InformeRadiologico;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.modelo.SolicitudPruebaDiagnostica;
import com.toni.hospital.modelo.TipoPruebaDiagnostica;
import com.toni.hospital.repositorio.InformeRadiologicoRepository;
import com.toni.hospital.repositorio.MedicoRepository;
import com.toni.hospital.repositorio.SolicitudPruebaDiagnosticaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/** Operaciones sobre informes radiológicos. */
@Service
public class InformeRadiologicoService {

    private final InformeRadiologicoRepository informeRepository;
    private final SolicitudPruebaDiagnosticaRepository solicitudRepository;
    private final MedicoRepository medicoRepository;

    public InformeRadiologicoService(
            InformeRadiologicoRepository informeRepository,
            SolicitudPruebaDiagnosticaRepository solicitudRepository,
            MedicoRepository medicoRepository
    ) {
        this.informeRepository = informeRepository;
        this.solicitudRepository = solicitudRepository;
        this.medicoRepository = medicoRepository;
    }

    /** Busca los informes asociados a una solicitud. */
    public List<InformeRadiologico> listarPorSolicitud(Long solicitudId) {
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Solicitud de prueba diagnóstica no encontrada"
            );
        }

        return informeRepository
                .findBySolicitudPrueba_IdOrderByFechaHoraInformeDesc(
                        solicitudId
                );
    }

    /** Busca un informe por ID. */
    public InformeRadiologico buscarPorId(Long id) {
        return informeRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Informe radiológico no encontrado"
                ));
    }

    /** Registra un informe pendiente de validación. */
    @Transactional
    public InformeRadiologico guardar(InformeRadiologicoRequest request) {
        SolicitudPruebaDiagnostica solicitud = solicitudRepository
                .findWithDetallesById(request.solicitudPruebaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud de prueba diagnóstica no encontrada"
                ));

        if (solicitud.getTipo() != TipoPruebaDiagnostica.RADIOLOGIA) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El informe solo puede asociarse a una solicitud "
                            + "de tipo RADIOLOGIA"
            );
        }

        Medico medico = medicoRepository
                .findById(request.medicoInformanteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico informante no encontrado"
                ));

        InformeRadiologico informe = new InformeRadiologico();
        informe.setSolicitudPrueba(solicitud);
        informe.setMedicoInformante(medico);
        informe.setFechaHoraEstudio(request.fechaHoraEstudio());
        informe.setFechaHoraInforme(LocalDateTime.now());
        informe.setHallazgos(request.hallazgos());
        informe.setConclusion(request.conclusion());
        informe.setEstado(EstadoInformeRadiologico.PENDIENTE_VALIDACION);

        return informeRepository.save(informe);
    }
}