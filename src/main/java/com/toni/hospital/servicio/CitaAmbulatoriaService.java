package com.toni.hospital.servicio;

import com.toni.hospital.dto.CitaAmbulatoriaRequest;
import com.toni.hospital.modelo.CitaAmbulatoria;
import com.toni.hospital.modelo.EstadoCitaAmbulatoria;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Paciente;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.repositorio.CitaAmbulatoriaRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.PacienteRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Coordina las operaciones relacionadas con citas ambulatorias.
 */
@Service
public class CitaAmbulatoriaService {

    private final CitaAmbulatoriaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final HospitalRepository hospitalRepository;
    private final ServicioHospitalarioRepository servicioRepository;

    public CitaAmbulatoriaService(
            CitaAmbulatoriaRepository citaRepository,
            PacienteRepository pacienteRepository,
            HospitalRepository hospitalRepository,
            ServicioHospitalarioRepository servicioRepository
    ) {
        this.citaRepository = citaRepository;
        this.pacienteRepository = pacienteRepository;
        this.hospitalRepository = hospitalRepository;
        this.servicioRepository = servicioRepository;
    }

    /** Devuelve todas las citas. */
    public List<CitaAmbulatoria> listarCitas() {
        return citaRepository.findAll();
    }

    /** Busca una cita por su ID o responde con 404 si no existe. */
    public CitaAmbulatoria buscarCitaPorId(Long id) {
        return citaRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cita ambulatoria no encontrada"
                ));
    }

    /**
     * Crea una cita a partir de los datos recibidos.
     * Comprueba que existan el paciente, el hospital y el servicio.
     */
    @Transactional
    public CitaAmbulatoria guardarCita(CitaAmbulatoriaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.pacienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Paciente no encontrado"
                ));

        Hospital hospital = hospitalRepository.findById(request.hospitalId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));

        ServicioHospitalario servicio =
                servicioRepository.findById(request.servicioId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Servicio hospitalario no encontrado"
                        ));

        if (!servicio.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pueden programar citas en un servicio inactivo"
            );
        }

        boolean servicioDisponibleEnHospital = hospital.getServicios()
                .stream()
                .anyMatch(servicioHospitalario ->
                        servicioHospitalario.getId().equals(servicio.getId())
                );

        if (!servicioDisponibleEnHospital) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El servicio no está asociado a ese hospital"
            );
        }

        CitaAmbulatoria cita = new CitaAmbulatoria();
        cita.setPaciente(paciente);
        cita.setHospital(hospital);
        cita.setServicio(servicio);
        cita.setTipo(request.tipo());
        cita.setFechaHoraProgramada(request.fechaHoraProgramada());
        cita.setMotivo(request.motivo());
        cita.setObservaciones(request.observaciones());
        cita.setEstado(EstadoCitaAmbulatoria.PROGRAMADA);

        return citaRepository.save(cita);
    }
}