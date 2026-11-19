package com.toni.hospital.servicio;

import com.toni.hospital.dto.InterconsultaRequest;
import com.toni.hospital.dto.CambiarEstadoInterconsultaRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.EstadoInterconsulta;
import com.toni.hospital.modelo.Interconsulta;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.InterconsultaRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestiona las solicitudes de valoración a otros servicios hospitalarios.
 */
@Service
public class InterconsultaService {

    private final InterconsultaRepository interconsultaRepository;
    private final AtencionUrgenciasRepository atencionUrgenciasRepository;
    private final ServicioHospitalarioRepository servicioHospitalarioRepository;

    public InterconsultaService(
            InterconsultaRepository interconsultaRepository,
            AtencionUrgenciasRepository atencionUrgenciasRepository,
            ServicioHospitalarioRepository servicioHospitalarioRepository
    ) {
        this.interconsultaRepository = interconsultaRepository;
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
        this.servicioHospitalarioRepository = servicioHospitalarioRepository;
    }

    /**
     * Lista las interconsultas de una atención.
     */
    @Transactional(readOnly = true)
    public List<Interconsulta> listarPorAtencion(Long atencionId) {
        comprobarAtencionExiste(atencionId);

        return interconsultaRepository
                .findByAtencionUrgencias_IdOrderByFechaSolicitudDesc(
                        atencionId
                );
    }

    /**
     * Busca una interconsulta y verifica que pertenezca a la atención indicada.
     */
    @Transactional(readOnly = true)
    public Interconsulta buscarPorAtencion(Long atencionId, Long id) {
        comprobarAtencionExiste(atencionId);

        Interconsulta interconsulta = buscarInterconsultaPorId(id);

        if (!interconsulta.getAtencionUrgencias().getId().equals(atencionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Interconsulta no encontrada para esa atención"
            );
        }

        return interconsulta;
    }

    /**
     * Crea una interconsulta vinculada a la atención de la ruta.
     */
    @Transactional
    public Interconsulta crearInterconsulta(
            Long atencionId,
            InterconsultaRequest request
    ) {
        AtencionUrgencias atencion = comprobarAtencionExiste(atencionId);

        ServicioHospitalario servicio = servicioHospitalarioRepository
                .findById(request.servicioDestinoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Servicio hospitalario no encontrado"
                ));

        Interconsulta interconsulta = new Interconsulta();
        interconsulta.setFechaSolicitud(LocalDateTime.now());
        interconsulta.setMotivo(request.motivo());
        interconsulta.setEstado(EstadoInterconsulta.PENDIENTE);
        interconsulta.setServicioDestino(servicio);

        // Mantiene ambos lados de la relación Atención–Interconsulta.
        atencion.agregarInterconsulta(interconsulta);

        return interconsultaRepository.save(interconsulta);
    }

    /**
     * Cambia el estado de una interconsulta.
     *
     * Las transiciones permitidas entre estados las definiremos
     * como una regla de negocio en el siguiente repaso.
     */
    @Transactional
    public Interconsulta cambiarEstado(
            Long atencionId,
            Long id,
            CambiarEstadoInterconsultaRequest request
    ) {
        Interconsulta interconsulta = buscarPorAtencion(atencionId, id);

        interconsulta.setEstado(request.estado());

        return interconsultaRepository.save(interconsulta);
    }

    /**
     * Busca una interconsulta por ID o responde con HTTP 404.
     */
    private Interconsulta buscarInterconsultaPorId(Long id) {
        return interconsultaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Interconsulta no encontrada"
                ));
    }

    /**
     * Busca la atención asociada o responde con HTTP 404.
     */
    private AtencionUrgencias comprobarAtencionExiste(Long atencionId) {
        return atencionUrgenciasRepository.findById(atencionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de urgencias no encontrada"
                ));
    }
}