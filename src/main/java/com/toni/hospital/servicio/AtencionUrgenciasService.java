package com.toni.hospital.servicio;

import com.toni.hospital.dto.AtencionUrgenciasRequest;
import com.toni.hospital.dto.CerrarAtencionUrgenciasRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.VinculoProfesionalAtencion;
import com.toni.hospital.modelo.UbicacionUrgencias;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.PacienteRepository;
import com.toni.hospital.repositorio.RestriccionConfidencialidadRepository;
import com.toni.hospital.repositorio.UbicacionUrgenciasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

/**
 * Gestiona las operaciones generales de las atenciones de Urgencias.
 */
@Service
public class AtencionUrgenciasService {

    private final AtencionUrgenciasRepository atencionUrgenciasRepository;
    private final PacienteRepository pacienteRepository;
    private final HospitalRepository hospitalRepository;
    private final UbicacionUrgenciasRepository ubicacionUrgenciasRepository;
    private final RestriccionConfidencialidadRepository
            restriccionConfidencialidadRepository;

    public AtencionUrgenciasService(
            AtencionUrgenciasRepository atencionUrgenciasRepository,
            PacienteRepository pacienteRepository,
            HospitalRepository hospitalRepository,
            UbicacionUrgenciasRepository ubicacionUrgenciasRepository,
            RestriccionConfidencialidadRepository
                    restriccionConfidencialidadRepository
    ) {
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
        this.pacienteRepository = pacienteRepository;
        this.hospitalRepository = hospitalRepository;
        this.ubicacionUrgenciasRepository = ubicacionUrgenciasRepository;
        this.restriccionConfidencialidadRepository =
                restriccionConfidencialidadRepository;
    }

    /**
     * Devuelve todas las atenciones registradas.
     */
    @Transactional(readOnly = true)
    public java.util.List<AtencionUrgencias> listarAtenciones() {
        return atencionUrgenciasRepository.findAll();
    }

    /**
     * Devuelve las atenciones que todavía no han finalizado.
     */
    @Transactional(readOnly = true)
    public java.util.List<AtencionUrgencias> listarAtencionesActivas() {
        return atencionUrgenciasRepository
                .findByFechaHoraFinUrgenciasIsNullOrderByFechaHoraLlegadaAsc();
    }

    /**
     * Busca una atención por su ID.
     *
     * @throws ResponseStatusException con HTTP 404 si no existe
     */
    @Transactional(readOnly = true)
    public AtencionUrgencias buscarAtencionPorId(Long id) {
        return atencionUrgenciasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de Urgencias no encontrada"
                ));
    }

    /**
     * Registra una atención nueva.
     *
     * La fecha de llegada y el código de llamamiento se generan
     * en el servidor.
     */
    @Transactional
    public AtencionUrgencias crearAtencion(
            AtencionUrgenciasRequest request
    ) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));

        Hospital hospital = hospitalRepository
                .findById(request.hospitalAtencionId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital de atención no encontrado"
                ));

        AtencionUrgencias atencion = new AtencionUrgencias();

        atencion.setFechaHoraLlegada(LocalDateTime.now());
        atencion.setCodigoLlamamiento(generarCodigoLlamamiento());

        atencion.setPaciente(paciente);
        atencion.setHospitalAtencion(hospital);
        atencion.setMotivoConsulta(request.motivoConsulta());
        atencion.setCircuitoUrgencias(request.circuitoUrgencias());
        atencion.setMedioLlegada(request.medioLlegada());
        atencion.setProcedencia(request.procedencia());

        atencion.setTipoCobertura(request.tipoCobertura());
        atencion.setEntidadCobertura(request.entidadCobertura());

        VinculoProfesionalAtencion vinculo =
                request.vinculoProfesional() == null
                        ? VinculoProfesionalAtencion.NO_APLICA
                        : request.vinculoProfesional();

        atencion.setVinculoProfesional(vinculo);
        atencion.setAccidenteLaboral(request.accidenteLaboral());
        atencion.setBajoCustodiaPolicial(request.bajoCustodiaPolicial());

        if (request.ubicacionActualId() != null) {
            UbicacionUrgencias ubicacion =
                    ubicacionUrgenciasRepository
                            .findById(request.ubicacionActualId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Ubicación de Urgencias no encontrada"
                            ));

            atencion.setUbicacionActual(ubicacion);
        }

        return atencionUrgenciasRepository.save(atencion);
    }

    /**
     * Cierra una atención y registra su resultado.
     */
    @Transactional
    public AtencionUrgencias cerrarAtencion(
            Long id,
            CerrarAtencionUrgenciasRequest request
    ) {
        AtencionUrgencias atencion = buscarAtencionPorId(id);

        if (atencion.getFechaHoraFinUrgencias() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La atención ya está cerrada"
            );
        }

        atencion.setFechaHoraFinUrgencias(LocalDateTime.now());
        atencion.setResultado(request.resultado());

        return atencionUrgenciasRepository.save(atencion);
    }

    /**
     * Comprueba si hay una restricción activa para el paciente
     * en el hospital donde se atiende.
     */
    @Transactional(readOnly = true)
    public boolean tieneRestriccionConfidencialidad(
            Long pacienteId,
            Long hospitalId
    ) {
        return restriccionConfidencialidadRepository
                .existsByPaciente_IdAndHospital_IdAndFechaHoraFinIsNull(
                        pacienteId,
                        hospitalId
                );
    }

    /**
     * Genera un código aleatorio de 16 caracteres hexadecimales.
     *
     * Es provisional para el proyecto de aprendizaje y no reproduce
     * el sistema interno de identificación de un hospital real.
     */
    private String generarCodigoLlamamiento() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16)
                .toUpperCase(Locale.ROOT);
    }
}