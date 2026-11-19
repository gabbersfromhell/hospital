package com.toni.hospital.servicio;

import com.toni.hospital.dto.TrasladoHospitalarioRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.ResultadoAtencionUrgencias;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.modelo.TrasladoHospitalario;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.ServicioHospitalarioRepository;
import com.toni.hospital.repositorio.TrasladoHospitalarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TrasladoHospitalarioService {

    private final TrasladoHospitalarioRepository trasladoRepository;
    private final AtencionUrgenciasRepository atencionRepository;
    private final HospitalRepository hospitalRepository;
    private final ServicioHospitalarioRepository servicioRepository;

    public TrasladoHospitalarioService(
            TrasladoHospitalarioRepository trasladoRepository,
            AtencionUrgenciasRepository atencionRepository,
            HospitalRepository hospitalRepository,
            ServicioHospitalarioRepository servicioRepository
    ) {
        this.trasladoRepository = trasladoRepository;
        this.atencionRepository = atencionRepository;
        this.hospitalRepository = hospitalRepository;
        this.servicioRepository = servicioRepository;
    }

    @Transactional(readOnly = true)
    public List<TrasladoHospitalario> listarPorAtencion(Long atencionId) {
        comprobarAtencion(atencionId);

        return trasladoRepository
                .findByAtencionUrgencias_IdOrderByFechaHoraSolicitudDesc(
                        atencionId
                );
    }

    @Transactional(readOnly = true)
    public TrasladoHospitalario buscarPorAtencion(
            Long atencionId,
            Long trasladoId
    ) {
        comprobarAtencion(atencionId);

        TrasladoHospitalario traslado = buscarTraslado(trasladoId);

        if (!traslado.getAtencionUrgencias().getId().equals(atencionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Traslado no encontrado para esa atención"
            );
        }

        return traslado;
    }

    @Transactional
    public TrasladoHospitalario solicitarTraslado(
            Long atencionId,
            TrasladoHospitalarioRequest request
    ) {
        AtencionUrgencias atencion = comprobarAtencion(atencionId);

        if (atencion.getFechaHoraFinUrgencias() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La atención ya ha finalizado"
            );
        }

        Hospital destino = hospitalRepository
                .findById(request.hospitalDestinoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital de destino no encontrado"
                ));

        if (destino.getId().equals(atencion.getHospitalAtencion().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El hospital de destino debe ser distinto del hospital de atención"
            );
        }

        ServicioHospitalario servicio = buscarServicioOpcional(
                request.servicioRequeridoId()
        );

        if (servicio != null
                && !hospitalRepository.existsByIdAndServicios_Id(
                destino.getId(),
                servicio.getId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El hospital de destino no ofrece ese servicio"
            );
        }

        TrasladoHospitalario traslado = new TrasladoHospitalario();
        traslado.setAtencionUrgencias(atencion);
        traslado.setHospitalDestino(destino);
        traslado.setServicioRequerido(servicio);
        traslado.setMotivoTraslado(request.motivoTraslado());
        traslado.setTipoTransporte(request.tipoTransporte());
        traslado.setFechaHoraSolicitud(LocalDateTime.now());
        traslado.setObservaciones(request.observaciones());

        atencion.agregarTrasladoHospitalario(traslado);

        return trasladoRepository.save(traslado);
    }

    /**
     * Registra la salida de la ambulancia y finaliza la atención
     * en el hospital de origen.
     */
    @Transactional
    public TrasladoHospitalario marcarSalida(
            Long atencionId,
            Long trasladoId
    ) {
        TrasladoHospitalario traslado =
                buscarPorAtencion(atencionId, trasladoId);

        if (traslado.getFechaHoraSalida() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La salida del traslado ya está registrada"
            );
        }

        if (traslado.getFechaHoraLlegada() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El traslado ya tiene registrada la llegada"
            );
        }

        LocalDateTime ahora = LocalDateTime.now();
        traslado.setFechaHoraSalida(ahora);

        AtencionUrgencias atencion = traslado.getAtencionUrgencias();
        atencion.setFechaHoraFinUrgencias(ahora);
        atencion.setResultado(
                ResultadoAtencionUrgencias.TRASLADO_A_OTRO_CENTRO
        );

        return trasladoRepository.save(traslado);
    }

    /**
     * Registra la llegada al hospital de destino.
     */
    @Transactional
    public TrasladoHospitalario marcarLlegada(
            Long atencionId,
            Long trasladoId
    ) {
        TrasladoHospitalario traslado =
                buscarPorAtencion(atencionId, trasladoId);

        if (traslado.getFechaHoraSalida() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Primero debe registrarse la salida"
            );
        }

        if (traslado.getFechaHoraLlegada() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La llegada del traslado ya está registrada"
            );
        }

        traslado.setFechaHoraLlegada(LocalDateTime.now());

        return trasladoRepository.save(traslado);
    }

    private TrasladoHospitalario buscarTraslado(Long id) {
        return trasladoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Traslado hospitalario no encontrado"
                ));
    }

    private AtencionUrgencias comprobarAtencion(Long atencionId) {
        return atencionRepository.findById(atencionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de Urgencias no encontrada"
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
                    "El servicio hospitalario está inactivo"
            );
        }

        return servicio;
    }
}