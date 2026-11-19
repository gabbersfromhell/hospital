package com.toni.hospital.servicio;

import com.toni.hospital.dto.MedicoRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.MedicoRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final HospitalRepository hospitalRepository;
    private final ServicioHospitalarioRepository servicioRepository;

    public MedicoService(
            MedicoRepository medicoRepository,
            HospitalRepository hospitalRepository,
            ServicioHospitalarioRepository servicioRepository
    ) {
        this.medicoRepository = medicoRepository;
        this.hospitalRepository = hospitalRepository;
        this.servicioRepository = servicioRepository;
    }

    @Transactional(readOnly = true)
    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Medico> listarMedicosActivos() {
        return medicoRepository.findByActivoTrue();
    }

    @Transactional(readOnly = true)
    public Medico buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico no encontrado"
                ));
    }

    @Transactional
    public Medico guardarMedico(MedicoRequest request) {
        comprobarColegiadoDisponible(request.numeroColegiado(), null);

        Hospital hospital = buscarHospital(request.hospitalId());
        ServicioHospitalario servicio =
                buscarServicio(request.servicioHospitalarioId());

        comprobarServicioOfrecidoPorHospital(
                hospital.getId(),
                servicio.getId()
        );

        Medico medico = new Medico();
        copiarDatos(request, medico);
        medico.setHospital(hospital);
        medico.setServicioHospitalario(servicio);

        return medicoRepository.save(medico);
    }

    @Transactional
    public Medico actualizarMedico(Long id, MedicoRequest request) {
        Medico medico = buscarMedicoPorId(id);

        comprobarColegiadoDisponible(request.numeroColegiado(), id);

        Hospital hospital = buscarHospital(request.hospitalId());
        ServicioHospitalario servicio =
                buscarServicio(request.servicioHospitalarioId());

        comprobarServicioOfrecidoPorHospital(
                hospital.getId(),
                servicio.getId()
        );

        copiarDatos(request, medico);
        medico.setHospital(hospital);
        medico.setServicioHospitalario(servicio);

        return medicoRepository.save(medico);
    }

    /**
     * DELETE lógico: mantiene las referencias históricas del médico.
     */
    @Transactional
    public void desactivarMedico(Long id) {
        Medico medico = buscarMedicoPorId(id);
        medico.setActivo(false);
        medicoRepository.save(medico);
    }

    private void copiarDatos(MedicoRequest request, Medico medico) {
        medico.setNombre(request.nombre());
        medico.setApellidos(request.apellidos());
        medico.setNumeroColegiado(request.numeroColegiado());
        medico.setActivo(request.activo());
    }

    private Hospital buscarHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }

    private ServicioHospitalario buscarServicio(Long servicioId) {
        ServicioHospitalario servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Servicio hospitalario no encontrado"
                ));

        if (!servicio.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El servicio hospitalario está inactivo"
            );
        }

        return servicio;
    }

    private void comprobarServicioOfrecidoPorHospital(
            Long hospitalId,
            Long servicioId
    ) {
        if (!hospitalRepository.existsByIdAndServicios_Id(
                hospitalId,
                servicioId
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ese hospital no ofrece el servicio indicado"
            );
        }
    }

    private void comprobarColegiadoDisponible(
            String numeroColegiado,
            Long medicoIdExcluido
    ) {
        boolean duplicado = medicoIdExcluido == null
                ? medicoRepository.existsByNumeroColegiado(numeroColegiado)
                : medicoRepository.existsByNumeroColegiadoAndIdNot(
                numeroColegiado,
                medicoIdExcluido
        );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un médico con ese número de colegiado"
            );
        }
    }
}