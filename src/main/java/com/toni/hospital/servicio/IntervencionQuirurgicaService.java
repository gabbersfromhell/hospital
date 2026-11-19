package com.toni.hospital.servicio;

import com.toni.hospital.dto.IntervencionQuirurgicaRequest;
import com.toni.hospital.modelo.*;
import com.toni.hospital.repositorio.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IntervencionQuirurgicaService {

    private final IntervencionQuirurgicaRepository intervencionRepository;
    private final PacienteRepository pacienteRepository;
    private final HospitalRepository hospitalRepository;
    private final ServicioHospitalarioRepository servicioRepository;
    private final SalaQuirurgicaRepository salaRepository;
    private final MedicoRepository medicoRepository;
    private final AtencionUrgenciasRepository atencionRepository;
    private final IngresoHospitalarioRepository ingresoRepository;
    private final CitaAmbulatoriaRepository citaRepository;

    public IntervencionQuirurgicaService(
            IntervencionQuirurgicaRepository intervencionRepository,
            PacienteRepository pacienteRepository,
            HospitalRepository hospitalRepository,
            ServicioHospitalarioRepository servicioRepository,
            SalaQuirurgicaRepository salaRepository,
            MedicoRepository medicoRepository,
            AtencionUrgenciasRepository atencionRepository,
            IngresoHospitalarioRepository ingresoRepository,
            CitaAmbulatoriaRepository citaRepository
    ) {
        this.intervencionRepository = intervencionRepository;
        this.pacienteRepository = pacienteRepository;
        this.hospitalRepository = hospitalRepository;
        this.servicioRepository = servicioRepository;
        this.salaRepository = salaRepository;
        this.medicoRepository = medicoRepository;
        this.atencionRepository = atencionRepository;
        this.ingresoRepository = ingresoRepository;
        this.citaRepository = citaRepository;
    }

    public List<IntervencionQuirurgica> listar() {
        return intervencionRepository.findAllByOrderByFechaHoraProgramadaAsc();
    }

    public IntervencionQuirurgica buscarPorId(Long id) {
        return intervencionRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Intervención quirúrgica no encontrada"
                ));
    }

    @Transactional
    public IntervencionQuirurgica guardar(
            IntervencionQuirurgicaRequest request
    ) {
        validarContextoUnico(request);
        IntervencionQuirurgica intervencion =
                new IntervencionQuirurgica();
        aplicarDatos(intervencion, request);
        intervencion.setEstado(EstadoIntervencionQuirurgica.PROGRAMADA);
        return intervencionRepository.save(intervencion);
    }

    @Transactional
    public IntervencionQuirurgica actualizar(
            Long id, IntervencionQuirurgicaRequest request
    ) {
        validarContextoUnico(request);
        IntervencionQuirurgica intervencion = buscarPorId(id);
        if (intervencion.getEstado() != EstadoIntervencionQuirurgica.PROGRAMADA
                && intervencion.getEstado()
                != EstadoIntervencionQuirurgica.CONFIRMADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se puede editar una intervención programada "
                            + "o confirmada"
            );
        }
        aplicarDatos(intervencion, request);
        return intervencionRepository.save(intervencion);
    }

    @Transactional
    public void cancelar(Long id) {
        IntervencionQuirurgica intervencion = buscarPorId(id);
        if (intervencion.getEstado()
                == EstadoIntervencionQuirurgica.EN_CURSO
                || intervencion.getEstado()
                == EstadoIntervencionQuirurgica.FINALIZADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede cancelar una intervención iniciada "
                            + "o finalizada"
            );
        }
        intervencion.setEstado(EstadoIntervencionQuirurgica.CANCELADA);
        intervencionRepository.save(intervencion);
    }

    private void aplicarDatos(
            IntervencionQuirurgica intervencion,
            IntervencionQuirurgicaRequest request
    ) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente no encontrado"
                ));
        Hospital hospital = hospitalRepository.findById(request.hospitalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Hospital no encontrado"
                ));
        ServicioHospitalario servicio =
                servicioRepository.findById(request.servicioQuirurgicoId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Servicio quirúrgico no encontrado"
                        ));
        SalaQuirurgica sala = salaRepository
                .findWithHospitalById(request.salaQuirurgicaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sala quirúrgica no encontrada"
                ));
        Medico medico = medicoRepository.findById(
                request.medicoResponsableId()
        ).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Médico responsable no encontrado"
        ));

        if (!sala.isActiva()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La sala quirúrgica está inactiva"
            );
        }
        if (!sala.getHospital().getId().equals(hospital.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La sala no pertenece al hospital indicado"
            );
        }
        if (!servicio.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El servicio está inactivo"
            );
        }
        boolean servicioOfrecidoPorHospital = hospital.getServicios()
                .stream()
                .anyMatch(servicioHospitalario ->
                        servicioHospitalario.getId().equals(servicio.getId())
                );
        if (!servicioOfrecidoPorHospital) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El hospital no ofrece el servicio quirúrgico indicado"
            );
        }
        if (request.fechaHoraFinProgramada() != null
                && !request.fechaHoraFinProgramada()
                .isAfter(request.fechaHoraProgramada())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El fin programado no puede ser anterior al inicio"
            );
        }

        intervencion.setPaciente(paciente);
        intervencion.setHospital(hospital);
        intervencion.setServicioQuirurgico(servicio);
        intervencion.setSalaQuirurgica(sala);
        intervencion.setMedicoResponsable(medico);
        intervencion.setNombreProcedimiento(
                request.nombreProcedimiento().trim()
        );
        intervencion.setFechaHoraProgramada(
                request.fechaHoraProgramada()
        );
        intervencion.setFechaHoraFinProgramada(
                request.fechaHoraFinProgramada()
        );
        intervencion.setObservaciones(request.observaciones());

        intervencion.setAtencionUrgencias(null);
        intervencion.setIngresoHospitalario(null);
        intervencion.setCitaAmbulatoria(null);

        if (request.atencionUrgenciasId() != null) {
            AtencionUrgencias atencion = atencionRepository
                    .findById(request.atencionUrgenciasId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Atención de urgencias no encontrada"
                    ));
            validarPacienteYHospital(
                    paciente, hospital,
                    atencion.getPaciente().getId(),
                    atencion.getHospitalAtencion().getId()
            );
            intervencion.setAtencionUrgencias(atencion);
        } else if (request.ingresoHospitalarioId() != null) {
            IngresoHospitalario ingreso = ingresoRepository
                    .findById(request.ingresoHospitalarioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ingreso hospitalario no encontrado"
                    ));
            AtencionUrgencias atencionOrigen = ingreso.getSolicitudIngreso()
                    .getAtencionUrgencias();
            validarPacienteYHospital(
                    paciente, hospital,
                    atencionOrigen.getPaciente().getId(),
                    atencionOrigen.getHospitalAtencion().getId()
            );
            intervencion.setIngresoHospitalario(ingreso);
        } else {
            CitaAmbulatoria cita = citaRepository
                    .findById(request.citaAmbulatoriaId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Cita ambulatoria no encontrada"
                    ));
            validarPacienteYHospital(
                    paciente, hospital,
                    cita.getPaciente().getId(),
                    cita.getHospital().getId()
            );
            intervencion.setCitaAmbulatoria(cita);
        }
    }

    private void validarPacienteYHospital(
            Paciente paciente,
            Hospital hospital,
            Long pacienteContextoId,
            Long hospitalContextoId
    ) {
        if (!paciente.getId().equals(pacienteContextoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El paciente no coincide con el contexto asistencial"
            );
        }
        if (!hospital.getId().equals(hospitalContextoId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El hospital no coincide con el contexto asistencial"
            );
        }
    }

    private void validarContextoUnico(
            IntervencionQuirurgicaRequest request
    ) {
        int cantidad = 0;
        if (request.atencionUrgenciasId() != null) cantidad++;
        if (request.ingresoHospitalarioId() != null) cantidad++;
        if (request.citaAmbulatoriaId() != null) cantidad++;

        if (cantidad != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica exactamente un contexto: urgencias, ingreso "
                            + "o cita ambulatoria"
            );
        }
    }
}
