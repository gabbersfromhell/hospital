package com.toni.hospital.servicio;

import com.toni.hospital.dto.ValoracionTriajeRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.ValoracionTriaje;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.ValoracionTriajeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ValoracionTriajeService {

    private final ValoracionTriajeRepository valoracionTriajeRepository;
    private final AtencionUrgenciasRepository atencionUrgenciasRepository;

    public ValoracionTriajeService(
            ValoracionTriajeRepository valoracionTriajeRepository,
            AtencionUrgenciasRepository atencionUrgenciasRepository
    ) {
        this.valoracionTriajeRepository = valoracionTriajeRepository;
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
    }

    /** Lista las valoraciones de una atención, de más reciente a más antigua. */
    @Transactional(readOnly = true)
    public List<ValoracionTriaje> listarPorAtencion(Long atencionId) {
        comprobarAtencionExiste(atencionId);

        return valoracionTriajeRepository
                .findByAtencionUrgencias_IdOrderByFechaHoraDesc(atencionId);
    }

    /**
     * Busca una valoración y comprueba que pertenece a la atención indicada.
     */
    @Transactional(readOnly = true)
    public ValoracionTriaje buscarPorAtencion(
            Long atencionId,
            Long valoracionId
    ) {
        comprobarAtencionExiste(atencionId);

        ValoracionTriaje valoracion =
                valoracionTriajeRepository.findById(valoracionId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Valoración de triaje no encontrada"
                        ));

        if (!valoracion.getAtencionUrgencias()
                .getId()
                .equals(atencionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Valoración no encontrada para esa atención"
            );
        }

        return valoracion;
    }

    /**
     * Registra una nueva valoración.
     * La fecha y hora las establece el servidor.
     */
    @Transactional
    public ValoracionTriaje crearValoracion(
            Long atencionId,
            ValoracionTriajeRequest request
    ) {
        AtencionUrgencias atencion =
                comprobarAtencionExiste(atencionId);

        ValoracionTriaje valoracion = new ValoracionTriaje();
        valoracion.setFechaHora(LocalDateTime.now());
        valoracion.setNivelTriaje(request.nivelTriaje());
        valoracion.setObservaciones(request.observaciones());

        // Actualiza la relación desde la atención y desde la valoración.
        atencion.agregarValoracionTriaje(valoracion);

        return valoracionTriajeRepository.save(valoracion);
    }

    /** Busca la atención o responde con HTTP 404. */
    private AtencionUrgencias comprobarAtencionExiste(Long atencionId) {
        return atencionUrgenciasRepository.findById(atencionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de Urgencias no encontrada"
                ));
    }
}