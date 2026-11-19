package com.toni.hospital.servicio;

import com.toni.hospital.dto.AtencionUrgenciasRequest;
import com.toni.hospital.dto.CerrarAtencionUrgenciasRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.UbicacionUrgencias;
import com.toni.hospital.modelo.VinculoProfesionalAtencion;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.PacienteRepository;
import com.toni.hospital.repositorio.UbicacionUrgenciasRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lógica de acceso y operaciones generales sobre atenciones de Urgencias.
 */
@Service
public class AtencionUrgenciasService {

    private final AtencionUrgenciasRepository atencionUrgenciasRepository;
    private final PacienteRepository pacienteRepository;
    private final HospitalRepository hospitalRepository;
    private final UbicacionUrgenciasRepository ubicacionUrgenciasRepository;

    public AtencionUrgenciasService(
            AtencionUrgenciasRepository atencionUrgenciasRepository,
            PacienteRepository pacienteRepository,
            HospitalRepository hospitalRepository,
            UbicacionUrgenciasRepository ubicacionUrgenciasRepository
    ) {
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
        this.pacienteRepository = pacienteRepository;
        this.hospitalRepository = hospitalRepository;
        this.ubicacionUrgenciasRepository = ubicacionUrgenciasRepository;
    }

    /**
     * Devuelve todas las atenciones.
     */
    @Transactional(readOnly = true)
    public List<AtencionUrgencias> listarAtenciones() {
        return atencionUrgenciasRepository.findAll();
    }

    /**
     * Devuelve el censo de atenciones que todavía no han finalizado.
     */
    @Transactional(readOnly = true)
    public List<AtencionUrgencias> listarAtencionesActivas() {
        return atencionUrgenciasRepository
                .findByFechaHoraFinUrgenciasIsNullOrderByFechaHoraLlegadaAsc();
    }

    /**
     * Busca una atención por su ID.
     */
    @Transactional(readOnly = true)
    public AtencionUrgencias buscarAtencionPorId(Long id) {
        return atencionUrgenciasRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de urgencias no encontrada"
                ));
    }

    /**
     * Registra una nueva atención.
     *
     * La hora de llegada la establece el servidor, no el cliente.
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
        atencion.setPaciente(paciente);
        atencion.setHospitalAtencion(hospital);
        atencion.setMotivoConsulta(request.motivoConsulta());
        atencion.setCircuitoUrgencias(request.circuitoUrgencias());
        atencion.setTipoCobertura(request.tipoCobertura());
        atencion.setEntidadCobertura(request.entidadCobertura());

        // Si el request no indica vínculo profesional, se usa NO_APLICA.
        VinculoProfesionalAtencion vinculo = request.vinculoProfesional() == null
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
     * Finaliza una atención y registra el resultado.
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
}