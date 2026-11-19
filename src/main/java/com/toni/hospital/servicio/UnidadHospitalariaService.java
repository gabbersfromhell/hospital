package com.toni.hospital.servicio;

import com.toni.hospital.dto.UnidadHospitalariaRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.modelo.UnidadHospitalaria;
import com.toni.hospital.repositorio.HabitacionRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import com.toni.hospital.repositorio.UnidadHospitalariaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UnidadHospitalariaService {

    private final UnidadHospitalariaRepository unidadRepository;
    private final HospitalRepository hospitalRepository;
    private final ServicioHospitalarioRepository servicioRepository;
    private final HabitacionRepository habitacionRepository;

    public UnidadHospitalariaService(
            UnidadHospitalariaRepository unidadRepository,
            HospitalRepository hospitalRepository,
            ServicioHospitalarioRepository servicioRepository,
            HabitacionRepository habitacionRepository
    ) {
        this.unidadRepository = unidadRepository;
        this.hospitalRepository = hospitalRepository;
        this.servicioRepository = servicioRepository;
        this.habitacionRepository = habitacionRepository;
    }

    @Transactional(readOnly = true)
    public List<UnidadHospitalaria> listarUnidades() {
        return unidadRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UnidadHospitalaria buscarUnidadPorId(Long id) {
        return unidadRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unidad hospitalaria no encontrada"
                ));
    }

    @Transactional
    public UnidadHospitalaria guardarUnidad(
            UnidadHospitalariaRequest request
    ) {
        Hospital hospital = buscarHospital(request.hospitalId());
        ServicioHospitalario servicio =
                buscarServicioOpcional(request.servicioHospitalarioId());

        comprobarCodigoDisponible(hospital.getId(), request.codigo(), null);

        UnidadHospitalaria unidad = new UnidadHospitalaria();
        unidad.setCodigo(request.codigo());
        unidad.setNombre(request.nombre());
        unidad.setHospital(hospital);
        unidad.setServicioHospitalario(servicio);
        unidad.setActiva(request.activa());

        return unidadRepository.save(unidad);
    }

    @Transactional
    public UnidadHospitalaria actualizarUnidad(
            Long id,
            UnidadHospitalariaRequest request
    ) {
        UnidadHospitalaria unidad = buscarUnidadPorId(id);
        Hospital hospital = buscarHospital(request.hospitalId());
        ServicioHospitalario servicio =
                buscarServicioOpcional(request.servicioHospitalarioId());

        comprobarCodigoDisponible(hospital.getId(), request.codigo(), id);

        if (!request.activa()
                && unidad.isActiva()
                && habitacionRepository
                .existsByUnidadHospitalaria_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reasigna las habitaciones antes de desactivar la unidad"
            );
        }

        unidad.setCodigo(request.codigo());
        unidad.setNombre(request.nombre());
        unidad.setHospital(hospital);
        unidad.setServicioHospitalario(servicio);
        unidad.setActiva(request.activa());

        return unidadRepository.save(unidad);
    }

    /**
     * Desactivación lógica: conserva el registro y sus referencias.
     */
    @Transactional
    public void desactivarUnidad(Long id) {
        UnidadHospitalaria unidad = buscarUnidadPorId(id);

        if (habitacionRepository.existsByUnidadHospitalaria_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Reasigna las habitaciones antes de desactivar la unidad"
            );
        }

        unidad.setActiva(false);
        unidadRepository.save(unidad);
    }

    private Hospital buscarHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }

    private ServicioHospitalario buscarServicioOpcional(Long servicioId) {
        if (servicioId == null) {
            return null;
        }

        ServicioHospitalario servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Servicio hospitalario no encontrado"
                ));

        if (!servicio.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede asociar un servicio inactivo"
            );
        }

        return servicio;
    }

    private void comprobarCodigoDisponible(
            Long hospitalId,
            String codigo,
            Long unidadIdExcluida
    ) {
        boolean duplicado = unidadIdExcluida == null
                ? unidadRepository.existsByHospital_IdAndCodigo(
                hospitalId,
                codigo
        )
                : unidadRepository.existsByHospital_IdAndCodigoAndIdNot(
                hospitalId,
                codigo,
                unidadIdExcluida
        );

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ese código ya está usado en el hospital"
            );
        }
    }
}