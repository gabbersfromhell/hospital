package com.toni.hospital.servicio;

import com.toni.hospital.dto.ServicioHospitalarioRequest;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ServicioHospitalarioService {

    private final ServicioHospitalarioRepository servicioHospitalarioRepository;

    public ServicioHospitalarioService(
            ServicioHospitalarioRepository servicioHospitalarioRepository
    ) {
        this.servicioHospitalarioRepository = servicioHospitalarioRepository;
    }

    public List<ServicioHospitalario> listarServicios() {
        return servicioHospitalarioRepository.findAll();
    }

    public ServicioHospitalario buscarServicioPorId(Long id) {
        return servicioHospitalarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Servicio hospitalario no encontrado"
                ));
    }

    @Transactional
    public ServicioHospitalario guardarServicio(
            ServicioHospitalarioRequest request
    ) {
        ServicioHospitalario servicio = new ServicioHospitalario();
        servicio.setNombre(request.nombre());
        servicio.setTipoServicio(request.tipoServicio());
        servicio.setActivo(request.activo());

        return servicioHospitalarioRepository.save(servicio);
    }

    @Transactional
    public ServicioHospitalario actualizarServicio(
            Long id,
            ServicioHospitalarioRequest request
    ) {
        ServicioHospitalario servicio = buscarServicioPorId(id);

        servicio.setNombre(request.nombre());
        servicio.setTipoServicio(request.tipoServicio());
        servicio.setActivo(request.activo());

        return servicioHospitalarioRepository.save(servicio);
    }

    @Transactional
    public void desactivarServicio(Long id) {
        ServicioHospitalario servicio = buscarServicioPorId(id);
        servicio.setActivo(false);
        servicioHospitalarioRepository.save(servicio);
    }
}