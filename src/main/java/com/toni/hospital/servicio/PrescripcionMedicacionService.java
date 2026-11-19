package com.toni.hospital.servicio;

import com.toni.hospital.dto.PrescripcionMedicacionRequest;
import com.toni.hospital.modelo.*;
import com.toni.hospital.repositorio.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/** Operaciones sobre prescripciones de medicación. */
@Service
public class PrescripcionMedicacionService {

    private final PrescripcionMedicacionRepository prescripcionRepository;
    private final MedicamentoRepository medicamentoRepository;
    private final MedicoRepository medicoRepository;
    private final AtencionUrgenciasRepository atencionRepository;
    private final IngresoHospitalarioRepository ingresoRepository;
    private final CitaAmbulatoriaRepository citaRepository;

    public PrescripcionMedicacionService(
            PrescripcionMedicacionRepository prescripcionRepository,
            MedicamentoRepository medicamentoRepository,
            MedicoRepository medicoRepository,
            AtencionUrgenciasRepository atencionRepository,
            IngresoHospitalarioRepository ingresoRepository,
            CitaAmbulatoriaRepository citaRepository
    ) {
        this.prescripcionRepository = prescripcionRepository;
        this.medicamentoRepository = medicamentoRepository;
        this.medicoRepository = medicoRepository;
        this.atencionRepository = atencionRepository;
        this.ingresoRepository = ingresoRepository;
        this.citaRepository = citaRepository;
    }

    /** Lista las prescripciones más recientes primero. */
    public List<PrescripcionMedicacion> listarPrescripciones() {
        return prescripcionRepository
                .findAllByOrderByFechaHoraPrescripcionDesc();
    }

    /** Busca una prescripción por ID. */
    public PrescripcionMedicacion buscarPorId(Long id) {
        return prescripcionRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Prescripción no encontrada"
                ));
    }

    /** Registra una nueva prescripción activa. */
    @Transactional
    public PrescripcionMedicacion guardar(
            PrescripcionMedicacionRequest request
    ) {
        validarContextoUnico(request);
        validarFechas(request);
        validarIndicacionSiPrecisa(request);
        validarDosisYUnidad(request);

        Medicamento medicamento = medicamentoRepository
                .findById(request.medicamentoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Medicamento no encontrado"
                ));

        if (!medicamento.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede prescribir un medicamento inactivo"
            );
        }

        Medico medico = medicoRepository
                .findById(request.medicoPrescriptorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico prescriptor no encontrado"
                ));

        PrescripcionMedicacion prescripcion =
                new PrescripcionMedicacion();

        prescripcion.setMedicamento(medicamento);
        prescripcion.setMedicoPrescriptor(medico);
        prescripcion.setDosisPorAdministracion(
                request.dosisPorAdministracion()
        );
        prescripcion.setUnidadDosis(request.unidadDosis());
        prescripcion.setViaAdministracion(request.viaAdministracion());
        prescripcion.setPauta(request.pauta());
        prescripcion.setSiPrecisa(request.siPrecisa());
        prescripcion.setIndicacionSiPrecisa(
                request.indicacionSiPrecisa()
        );
        prescripcion.setIndicacionClinica(request.indicacionClinica());
        prescripcion.setFechaHoraPrescripcion(LocalDateTime.now());
        prescripcion.setFechaHoraInicio(request.fechaHoraInicio());
        prescripcion.setFechaHoraFin(request.fechaHoraFin());
        prescripcion.setEstado(EstadoPrescripcionMedicacion.ACTIVA);

        asignarContexto(request, prescripcion);

        return prescripcionRepository.save(prescripcion);
    }

    /** Exige que se indique exactamente uno de los tres contextos. */
    private void validarContextoUnico(
            PrescripcionMedicacionRequest request
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

    /** Comprueba que el intervalo de vigencia tenga sentido. */
    private void validarFechas(PrescripcionMedicacionRequest request) {
        if (request.fechaHoraFin() != null
                && request.fechaHoraFin()
                .isBefore(request.fechaHoraInicio())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de fin no puede ser anterior a la de inicio"
            );
        }
    }

    /** Exige una indicación cuando la pauta es "si precisa". */
    private void validarIndicacionSiPrecisa(
            PrescripcionMedicacionRequest request
    ) {
        boolean indicacionVacia =
                request.indicacionSiPrecisa() == null
                        || request.indicacionSiPrecisa().isBlank();

        if (request.siPrecisa() && indicacionVacia) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica en qué situación se utiliza la pauta "
                            + "si precisa"
            );
        }

        if (!request.siPrecisa() && !indicacionVacia) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La indicación si precisa solo se usa cuando "
                            + "siPrecisa es true"
            );
        }
    }

    /** Comprueba que la dosis numérica y su unidad se envíen juntas. */
    private void validarDosisYUnidad(
            PrescripcionMedicacionRequest request
    ) {
        boolean tieneDosis = request.dosisPorAdministracion() != null;
        boolean tieneUnidad = request.unidadDosis() != null
                && !request.unidadDosis().isBlank();

        if (tieneDosis != tieneUnidad) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La dosis numérica y su unidad deben indicarse juntas"
            );
        }
    }

    /** Busca y asigna el contexto asistencial seleccionado. */
    private void asignarContexto(
            PrescripcionMedicacionRequest request,
            PrescripcionMedicacion prescripcion
    ) {
        if (request.atencionUrgenciasId() != null) {
            AtencionUrgencias atencion = atencionRepository
                    .findById(request.atencionUrgenciasId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Atención de urgencias no encontrada"
                    ));

            prescripcion.setAtencionUrgencias(atencion);
        }

        if (request.ingresoHospitalarioId() != null) {
            IngresoHospitalario ingreso = ingresoRepository
                    .findById(request.ingresoHospitalarioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ingreso hospitalario no encontrado"
                    ));

            prescripcion.setIngresoHospitalario(ingreso);
        }

        if (request.citaAmbulatoriaId() != null) {
            CitaAmbulatoria cita = citaRepository
                    .findById(request.citaAmbulatoriaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cita ambulatoria no encontrada"
                    ));

            prescripcion.setCitaAmbulatoria(cita);
        }
    }
}