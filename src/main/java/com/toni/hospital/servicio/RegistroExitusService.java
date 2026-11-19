package com.toni.hospital.servicio;

import com.toni.hospital.dto.AvisoFunerariaRequest;
import com.toni.hospital.dto.CertificadoExitusRequest;
import com.toni.hospital.dto.EntradaMorgueRequest;
import com.toni.hospital.dto.RegistroExitusRequest;
import com.toni.hospital.dto.RetiradaMorgueRequest;
import com.toni.hospital.modelo.*;
import com.toni.hospital.repositorio.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistroExitusService {

    private final RegistroExitusRepository registroExitusRepository;
    private final PacienteRepository pacienteRepository;
    private final AtencionUrgenciasRepository atencionUrgenciasRepository;
    private final IngresoHospitalarioRepository ingresoHospitalarioRepository;
    private final MedicoRepository medicoRepository;
    private final NeveraMorgueRepository neveraMorgueRepository;
    private final AsignacionCamaRepository asignacionCamaRepository;

    public RegistroExitusService(
            RegistroExitusRepository registroExitusRepository,
            PacienteRepository pacienteRepository,
            AtencionUrgenciasRepository atencionUrgenciasRepository,
            IngresoHospitalarioRepository ingresoHospitalarioRepository,
            MedicoRepository medicoRepository,
            NeveraMorgueRepository neveraMorgueRepository,
            AsignacionCamaRepository asignacionCamaRepository
    ) {
        this.registroExitusRepository = registroExitusRepository;
        this.pacienteRepository = pacienteRepository;
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
        this.ingresoHospitalarioRepository = ingresoHospitalarioRepository;
        this.medicoRepository = medicoRepository;
        this.neveraMorgueRepository = neveraMorgueRepository;
        this.asignacionCamaRepository = asignacionCamaRepository;
    }

    @Transactional(readOnly = true)
    public List<RegistroExitus> listarRegistros() {
        return registroExitusRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RegistroExitus buscarPorId(Long id) {
        return registroExitusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Registro de exitus no encontrado"
                ));
    }

    /**
     * Registra un exitus ocurrido durante una atención de Urgencias
     * o durante un ingreso hospitalario.
     */
    @Transactional
    public RegistroExitus registrarExitus(RegistroExitusRequest request) {
        validarContextoUnico(request);
        validarFechasIniciales(request);

        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));

        if (registroExitusRepository.existsByPaciente_Id(paciente.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un registro de exitus para este paciente"
            );
        }

        AtencionUrgencias atencion;
        IngresoHospitalario ingreso = null;

        if (request.atencionUrgenciasId() != null) {
            atencion = atencionUrgenciasRepository
                    .findById(request.atencionUrgenciasId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Atención de Urgencias no encontrada"
                    ));

            validarPacienteDeAtencion(paciente, atencion);
            validarAtencionAbierta(
                    atencion,
                    request.fechaHoraFallecimiento()
            );
        } else {
            ingreso = ingresoHospitalarioRepository
                    .findById(request.ingresoHospitalarioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ingreso hospitalario no encontrado"
                    ));

            atencion = ingreso.getSolicitudIngreso()
                    .getAtencionUrgencias();

            validarPacienteDeAtencion(paciente, atencion);
            validarIngresoAbierto(
                    ingreso,
                    request.fechaHoraFallecimiento()
            );
        }

        Hospital hospital = atencion.getHospitalAtencion();

        Medico medico = buscarMedicoCertificador(
                request.medicoCertificadorId(),
                hospital
        );

        NeveraMorgue nevera = buscarNeveraDisponible(
                request.neveraMorgueId(),
                request.fechaHoraEntradaMorgue(),
                request.fechaHoraFallecimiento(),
                hospital
        );

        RegistroExitus registro = new RegistroExitus();
        registro.setPaciente(paciente);
        registro.setAtencionUrgencias(
                request.atencionUrgenciasId() != null ? atencion : null
        );
        registro.setIngresoHospitalario(ingreso);
        registro.setFechaHoraFallecimiento(
                request.fechaHoraFallecimiento()
        );
        registro.setMedicoCertificador(medico);
        registro.setFechaHoraCertificadoMedico(
                request.fechaHoraCertificadoMedico()
        );
        registro.setNeveraMorgue(nevera);
        registro.setFechaHoraEntradaMorgue(
                request.fechaHoraEntradaMorgue()
        );
        registro.setNombreFuneraria(request.nombreFuneraria());
        registro.setFechaHoraAvisoFuneraria(
                request.fechaHoraAvisoFuneraria()
        );
        registro.setFechaHoraRetirada(request.fechaHoraRetirada());

        if (ingreso == null) {
            // El fallecimiento cierra la atención de Urgencias.
            atencion.setFechaHoraFinUrgencias(
                    request.fechaHoraFallecimiento()
            );
            atencion.setResultado(ResultadoAtencionUrgencias.EXITUS);
        } else {
            // El fallecimiento finaliza el ingreso.
            ingreso.setFechaHoraAlta(
                    request.fechaHoraFallecimiento()
            );
            ingreso.setTipoAlta(TipoAltaHospitalaria.EXITUS);

            /*
             * Si también se registra la entrada en morgue,
             * cerramos la asignación de cama en ese momento.
             */
            if (request.fechaHoraEntradaMorgue() != null) {
                cerrarAsignacionesCama(
                        ingreso,
                        request.fechaHoraEntradaMorgue()
                );
            }
        }

        return registroExitusRepository.save(registro);
    }

    /** Registra el médico y el momento en que se cumplimenta el certificado. */
    @Transactional
    public RegistroExitus registrarCertificado(
            Long id,
            CertificadoExitusRequest request
    ) {
        RegistroExitus registro = buscarPorId(id);

        if (registro.getFechaHoraCertificadoMedico() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El certificado ya está registrado"
            );
        }

        LocalDateTime fechaCertificado =
                request.fechaHoraCertificadoMedico();

        if (fechaCertificado.isBefore(
                registro.getFechaHoraFallecimiento()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El certificado no puede ser anterior al fallecimiento"
            );
        }

        Hospital hospital = obtenerHospitalDelRegistro(registro);
        Medico medico = buscarMedicoCertificador(
                request.medicoCertificadorId(),
                hospital
        );

        registro.setMedicoCertificador(medico);
        registro.setFechaHoraCertificadoMedico(fechaCertificado);

        return registroExitusRepository.save(registro);
    }

    /** Registra la nevera y el momento de entrada en la morgue. */
    @Transactional
    public RegistroExitus registrarEntradaMorgue(
            Long id,
            EntradaMorgueRequest request
    ) {
        RegistroExitus registro = buscarPorId(id);

        if (registro.getFechaHoraEntradaMorgue() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La entrada en morgue ya está registrada"
            );
        }

        LocalDateTime fechaEntrada = request.fechaHoraEntradaMorgue();

        if (fechaEntrada.isBefore(registro.getFechaHoraFallecimiento())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La entrada en morgue no puede ser anterior al fallecimiento"
            );
        }

        NeveraMorgue nevera = neveraMorgueRepository
                .findById(request.neveraMorgueId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nevera de morgue no encontrada"
                ));

        validarNeveraParaHospital(
                nevera,
                obtenerHospitalDelRegistro(registro)
        );

        boolean ocupadaPorOtroRegistro = registroExitusRepository
                .existsByNeveraMorgue_IdAndFechaHoraRetiradaIsNullAndIdNot(
                        nevera.getId(),
                        registro.getId()
                );

        if (ocupadaPorOtroRegistro) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La nevera está ocupada por otro paciente"
            );
        }

        registro.setNeveraMorgue(nevera);
        registro.setFechaHoraEntradaMorgue(fechaEntrada);

        if (registro.getIngresoHospitalario() != null) {
            cerrarAsignacionesCama(
                    registro.getIngresoHospitalario(),
                    fechaEntrada
            );
        }

        return registroExitusRepository.save(registro);
    }

    /** Registra a qué funeraria se avisó y cuándo. */
    @Transactional
    public RegistroExitus registrarAvisoFuneraria(
            Long id,
            AvisoFunerariaRequest request
    ) {
        RegistroExitus registro = buscarPorId(id);

        if (registro.getFechaHoraEntradaMorgue() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Primero registra la entrada del paciente en la morgue"
            );
        }

        if (registro.getFechaHoraAvisoFuneraria() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El aviso a la funeraria ya está registrado"
            );
        }

        LocalDateTime fechaAviso = request.fechaHoraAvisoFuneraria();

        if (fechaAviso.isBefore(registro.getFechaHoraFallecimiento())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El aviso no puede ser anterior al fallecimiento"
            );
        }

        registro.setNombreFuneraria(request.nombreFuneraria());
        registro.setFechaHoraAvisoFuneraria(fechaAviso);

        return registroExitusRepository.save(registro);
    }

    /** Registra la retirada del paciente por parte de la funeraria. */
    @Transactional
    public RegistroExitus registrarRetirada(
            Long id,
            RetiradaMorgueRequest request
    ) {
        RegistroExitus registro = buscarPorId(id);

        if (registro.getFechaHoraEntradaMorgue() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No consta la entrada del paciente en la morgue"
            );
        }

        if (registro.getFechaHoraAvisoFuneraria() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Primero registra el aviso a la funeraria"
            );
        }

        if (registro.getFechaHoraRetirada() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La retirada ya está registrada"
            );
        }

        LocalDateTime fechaRetirada = request.fechaHoraRetirada();

        if (fechaRetirada.isBefore(registro.getFechaHoraEntradaMorgue())
                || fechaRetirada.isBefore(
                registro.getFechaHoraAvisoFuneraria()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La retirada no puede ser anterior a la entrada o al aviso"
            );
        }

        registro.setFechaHoraRetirada(fechaRetirada);

        return registroExitusRepository.save(registro);
    }

    private void validarContextoUnico(RegistroExitusRequest request) {
        boolean tieneAtencion =
                request.atencionUrgenciasId() != null;
        boolean tieneIngreso =
                request.ingresoHospitalarioId() != null;

        if (tieneAtencion == tieneIngreso) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica una atención de Urgencias o un ingreso, pero no ambos"
            );
        }
    }

    private void validarFechasIniciales(RegistroExitusRequest request) {
        LocalDateTime fallecimiento =
                request.fechaHoraFallecimiento();

        if (fallecimiento.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha del fallecimiento no puede estar en el futuro"
            );
        }

        if ((request.medicoCertificadorId() == null)
                != (request.fechaHoraCertificadoMedico() == null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El médico y la fecha del certificado deben indicarse juntos"
            );
        }

        if (request.fechaHoraCertificadoMedico() != null
                && request.fechaHoraCertificadoMedico()
                .isBefore(fallecimiento)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El certificado no puede ser anterior al fallecimiento"
            );
        }

        if ((request.neveraMorgueId() == null)
                != (request.fechaHoraEntradaMorgue() == null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nevera y la fecha de entrada en morgue deben indicarse juntas"
            );
        }

        if (request.fechaHoraEntradaMorgue() != null
                && request.fechaHoraEntradaMorgue().isBefore(fallecimiento)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La entrada en morgue no puede ser anterior al fallecimiento"
            );
        }

        if (request.fechaHoraAvisoFuneraria() != null
                && (request.nombreFuneraria() == null
                || request.nombreFuneraria().isBlank())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica la funeraria a la que se avisa"
            );
        }

        if (request.fechaHoraAvisoFuneraria() != null
                && request.fechaHoraAvisoFuneraria().isBefore(fallecimiento)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El aviso no puede ser anterior al fallecimiento"
            );
        }

        if (request.fechaHoraRetirada() != null
                && (request.fechaHoraEntradaMorgue() == null
                || request.fechaHoraAvisoFuneraria() == null)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Para registrar la retirada deben constar la entrada en morgue y el aviso"
            );
        }

        if (request.fechaHoraRetirada() != null
                && (request.fechaHoraRetirada()
                .isBefore(request.fechaHoraEntradaMorgue())
                || request.fechaHoraRetirada()
                .isBefore(request.fechaHoraAvisoFuneraria()))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La retirada no puede ser anterior a la entrada o al aviso"
            );
        }
    }

    private void validarPacienteDeAtencion(
            Paciente paciente,
            AtencionUrgencias atencion
    ) {
        if (!atencion.getPaciente().getId().equals(paciente.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La atención indicada no pertenece a ese paciente"
            );
        }
    }

    private void validarAtencionAbierta(
            AtencionUrgencias atencion,
            LocalDateTime fechaFallecimiento
    ) {
        if (atencion.getFechaHoraFinUrgencias() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La atención de Urgencias ya está cerrada"
            );
        }

        if (fechaFallecimiento.isBefore(atencion.getFechaHoraLlegada())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El fallecimiento no puede ser anterior a la llegada"
            );
        }
    }

    private void validarIngresoAbierto(
            IngresoHospitalario ingreso,
            LocalDateTime fechaFallecimiento
    ) {
        if (ingreso.getFechaHoraAlta() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El ingreso hospitalario ya está cerrado"
            );
        }

        if (fechaFallecimiento.isBefore(ingreso.getFechaHoraIngreso())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El fallecimiento no puede ser anterior al ingreso"
            );
        }
    }

    private Medico buscarMedicoCertificador(
            Long medicoId,
            Hospital hospital
    ) {
        if (medicoId == null) {
            return null;
        }

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico certificador no encontrado"
                ));

        if (!medico.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El médico certificador está inactivo"
            );
        }

        if (!medico.getHospital().getId().equals(hospital.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El médico no pertenece al hospital de la atención"
            );
        }

        return medico;
    }

    private NeveraMorgue buscarNeveraDisponible(
            Long neveraId,
            LocalDateTime fechaEntrada,
            LocalDateTime fechaFallecimiento,
            Hospital hospital
    ) {
        if (neveraId == null) {
            return null;
        }

        NeveraMorgue nevera = neveraMorgueRepository.findById(neveraId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nevera de morgue no encontrada"
                ));

        validarNeveraParaHospital(nevera, hospital);

        if (fechaEntrada == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica la fecha de entrada en morgue"
            );
        }

        if (fechaEntrada.isBefore(fechaFallecimiento)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La entrada en morgue no puede ser anterior al fallecimiento"
            );
        }

        boolean ocupada = registroExitusRepository
                .existsByNeveraMorgue_IdAndFechaHoraRetiradaIsNull(
                        neveraId
                );

        if (ocupada) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La nevera está ocupada por otro paciente"
            );
        }

        return nevera;
    }

    private void validarNeveraParaHospital(
            NeveraMorgue nevera,
            Hospital hospital
    ) {
        if (!nevera.isActiva()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La nevera de morgue está inactiva"
            );
        }

        if (!nevera.getHospital().getId().equals(hospital.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La nevera no pertenece al hospital de la atención"
            );
        }
    }

    private Hospital obtenerHospitalDelRegistro(
            RegistroExitus registro
    ) {
        AtencionUrgencias atencion = registro.getAtencionUrgencias();

        if (atencion == null
                && registro.getIngresoHospitalario() != null) {
            atencion = registro.getIngresoHospitalario()
                    .getSolicitudIngreso()
                    .getAtencionUrgencias();
        }

        if (atencion == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El registro no tiene una atención hospitalaria asociada"
            );
        }

        return atencion.getHospitalAtencion();
    }

    private void cerrarAsignacionesCama(
            IngresoHospitalario ingreso,
            LocalDateTime fechaHoraEntradaMorgue
    ) {
        List<AsignacionCama> asignacionesActivas =
                asignacionCamaRepository
                        .findByIngresoHospitalario_IdAndFechaHoraFinIsNull(
                                ingreso.getId()
                        );

        for (AsignacionCama asignacion : asignacionesActivas) {
            if (fechaHoraEntradaMorgue
                    .isBefore(asignacion.getFechaHoraInicio())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La entrada en morgue es anterior a la asignación de cama"
                );
            }

            asignacion.setFechaHoraFin(fechaHoraEntradaMorgue);
        }
    }
}