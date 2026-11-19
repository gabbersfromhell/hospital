package com.toni.hospital.servicio;

import com.toni.hospital.dto.ValoracionTriajeRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.Enfermero;
import com.toni.hospital.modelo.ValoracionTriaje;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.EnfermeroRepository;
import com.toni.hospital.repositorio.ValoracionTriajeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gestiona las valoraciones de triaje.
 */
@Service
public class ValoracionTriajeService {

    private final ValoracionTriajeRepository valoracionTriajeRepository;
    private final AtencionUrgenciasRepository atencionUrgenciasRepository;
    private final EnfermeroRepository enfermeroRepository;

    public ValoracionTriajeService(
            ValoracionTriajeRepository valoracionTriajeRepository,
            AtencionUrgenciasRepository atencionUrgenciasRepository,
            EnfermeroRepository enfermeroRepository
    ) {
        this.valoracionTriajeRepository = valoracionTriajeRepository;
        this.atencionUrgenciasRepository = atencionUrgenciasRepository;
        this.enfermeroRepository = enfermeroRepository;
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
     * Registra una valoración y la vincula con la atención y el enfermero.
     * La fecha y hora las establece el servidor.
     */
    @Transactional
    public ValoracionTriaje crearValoracion(
            Long atencionId,
            ValoracionTriajeRequest request
    ) {
        AtencionUrgencias atencion =
                comprobarAtencionExiste(atencionId);

        Enfermero enfermero =
                enfermeroRepository.findById(request.enfermeroId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Enfermero no encontrado"
                        ));

        ValoracionTriaje valoracion = new ValoracionTriaje();
        valoracion.setFechaHora(LocalDateTime.now());
        valoracion.setNivelTriaje(request.nivelTriaje());
        valoracion.setObservaciones(request.observaciones());
        valoracion.setEnfermero(enfermero);

        // Establece la relación entre la valoración y la atención.
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