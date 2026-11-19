package com.toni.hospital.servicio;

import com.toni.hospital.dto.ProgramarAdministracionMedicacionRequest;
import com.toni.hospital.dto.RegistrarAdministracionMedicacionRequest;
import com.toni.hospital.modelo.AdministracionMedicacion;
import com.toni.hospital.modelo.Enfermero;
import com.toni.hospital.modelo.EstadoAdministracionMedicacion;
import com.toni.hospital.modelo.EstadoPrescripcionMedicacion;
import com.toni.hospital.modelo.PrescripcionMedicacion;
import com.toni.hospital.repositorio.AdministracionMedicacionRepository;
import com.toni.hospital.repositorio.EnfermeroRepository;
import com.toni.hospital.repositorio.PrescripcionMedicacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/** Operaciones sobre dosis programadas y sus resultados. */
@Service
public class AdministracionMedicacionService {

    private final AdministracionMedicacionRepository administracionRepository;
    private final PrescripcionMedicacionRepository prescripcionRepository;
    private final EnfermeroRepository enfermeroRepository;

    public AdministracionMedicacionService(
            AdministracionMedicacionRepository administracionRepository,
            PrescripcionMedicacionRepository prescripcionRepository,
            EnfermeroRepository enfermeroRepository
    ) {
        this.administracionRepository = administracionRepository;
        this.prescripcionRepository = prescripcionRepository;
        this.enfermeroRepository = enfermeroRepository;
    }

    /** Lista las administraciones de una prescripción. */
    public List<AdministracionMedicacion> listarPorPrescripcion(
            Long prescripcionId
    ) {
        if (!prescripcionRepository.existsById(prescripcionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Prescripción no encontrada"
            );
        }

        return administracionRepository
                .findByPrescripcionMedicacion_IdOrderByFechaHoraProgramadaAsc(
                        prescripcionId
                );
    }

    /** Busca una administración por ID. */
    public AdministracionMedicacion buscarPorId(Long id) {
        return administracionRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Administración de medicación no encontrada"
                ));
    }

    /** Programa una dosis de una prescripción activa. */
    @Transactional
    public AdministracionMedicacion programar(
            ProgramarAdministracionMedicacionRequest request
    ) {
        PrescripcionMedicacion prescripcion = prescripcionRepository
                .findWithDetallesById(request.prescripcionMedicacionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prescripción no encontrada"
                ));

        if (prescripcion.getEstado()
                != EstadoPrescripcionMedicacion.ACTIVA) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se pueden programar dosis de una prescripción activa"
            );
        }

        if (request.fechaHoraProgramada()
                .isBefore(prescripcion.getFechaHoraInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La administración programada es anterior al inicio "
                            + "de la prescripción"
            );
        }

        if (prescripcion.getFechaHoraFin() != null
                && request.fechaHoraProgramada()
                .isAfter(prescripcion.getFechaHoraFin())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La administración programada es posterior al fin "
                            + "de la prescripción"
            );
        }

        AdministracionMedicacion administracion =
                new AdministracionMedicacion();

        prescripcion.agregarAdministracion(administracion);
        administracion.setFechaHoraProgramada(
                request.fechaHoraProgramada()
        );
        administracion.setEstado(
                EstadoAdministracionMedicacion.PROGRAMADA
        );

        return administracionRepository.save(administracion);
    }

    /**
     * Registra si una dosis programada se administró,
     * se omitió o fue rechazada.
     */
    @Transactional
    public AdministracionMedicacion registrarResultado(
            Long administracionId,
            RegistrarAdministracionMedicacionRequest request
    ) {
        AdministracionMedicacion administracion =
                administracionRepository.findWithDetallesById(
                                administracionId
                        )
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Administración de medicación no encontrada"
                        ));

        if (administracion.getEstado()
                != EstadoAdministracionMedicacion.PROGRAMADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta administración ya tiene un resultado registrado"
            );
        }

        if (request.estado()
                == EstadoAdministracionMedicacion.PROGRAMADA) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El resultado no puede volver al estado PROGRAMADA"
            );
        }

        Enfermero enfermero = enfermeroRepository
                .findById(request.enfermeroResponsableId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Enfermero/a no encontrado/a"
                ));

        validarResultado(request);

        administracion.setEstado(request.estado());
        administracion.setEnfermeroResponsable(enfermero);
        administracion.setFechaHoraAdministracion(
                request.fechaHoraAdministracion()
        );
        administracion.setDosisAdministrada(
                request.dosisAdministrada()
        );
        administracion.setUnidadDosis(request.unidadDosis());
        administracion.setMotivoNoAdministracion(
                request.motivoNoAdministracion()
        );
        administracion.setObservaciones(request.observaciones());
        administracion.setFechaHoraRegistroResultado(
                LocalDateTime.now()
        );

        return administracionRepository.save(administracion);
    }

    /** Valida los campos requeridos según el resultado registrado. */
    private void validarResultado(
            RegistrarAdministracionMedicacionRequest request
    ) {
        boolean administrada = request.estado()
                == EstadoAdministracionMedicacion.ADMINISTRADA;

        if (administrada) {
            boolean faltaHora =
                    request.fechaHoraAdministracion() == null;
            boolean faltaDosis =
                    request.dosisAdministrada() == null;
            boolean faltaUnidad =
                    request.unidadDosis() == null
                            || request.unidadDosis().isBlank();

            if (faltaHora || faltaDosis || faltaUnidad) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Para registrar una dosis administrada se requieren "
                                + "la hora real, la dosis y su unidad"
                );
            }

            if (request.motivoNoAdministracion() != null
                    && !request.motivoNoAdministracion().isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Una dosis administrada no necesita motivo "
                                + "de no administración"
                );
            }

            return;
        }

        boolean faltaMotivo =
                request.motivoNoAdministracion() == null
                        || request.motivoNoAdministracion().isBlank();

        boolean incluyeDatosDeDosis =
                request.fechaHoraAdministracion() != null
                        || request.dosisAdministrada() != null
                        || (request.unidadDosis() != null
                        && !request.unidadDosis().isBlank());

        if (faltaMotivo || incluyeDatosDeDosis) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Para una dosis omitida o rechazada se requiere "
                            + "el motivo y no se deben enviar datos "
                            + "de administración"
            );
        }
    }
}