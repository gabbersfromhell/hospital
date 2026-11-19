package com.toni.hospital.controlador;

import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.ValoracionTriajeRequest;
import com.toni.hospital.dto.ValoracionTriajeResponse;
import com.toni.hospital.modelo.Enfermero;
import com.toni.hospital.modelo.ValoracionTriaje;
import com.toni.hospital.servicio.ValoracionTriajeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints para consultar y registrar valoraciones de triaje.
 */
@RestController
@RequestMapping(
        "/atenciones-urgencias/{atencionId}/valoraciones-triaje"
)
public class ValoracionTriajeController {

    private final ValoracionTriajeService valoracionTriajeService;

    public ValoracionTriajeController(
            ValoracionTriajeService valoracionTriajeService
    ) {
        this.valoracionTriajeService = valoracionTriajeService;
    }

    /** Lista las valoraciones de una atención. */
    @GetMapping
    public List<ValoracionTriajeResponse> listarPorAtencion(
            @PathVariable Long atencionId
    ) {
        return valoracionTriajeService.listarPorAtencion(atencionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca una valoración concreta dentro de esa atención. */
    @GetMapping("/{valoracionId}")
    public ValoracionTriajeResponse buscarPorAtencion(
            @PathVariable Long atencionId,
            @PathVariable Long valoracionId
    ) {
        ValoracionTriaje valoracion =
                valoracionTriajeService.buscarPorAtencion(
                        atencionId,
                        valoracionId
                );

        return convertirAResponse(valoracion);
    }

    /** Registra una valoración y responde con HTTP 201 Created. */
    @PostMapping
    public ResponseEntity<ValoracionTriajeResponse> crearValoracion(
            @PathVariable Long atencionId,
            @Valid @RequestBody ValoracionTriajeRequest request
    ) {
        ValoracionTriaje guardada =
                valoracionTriajeService.crearValoracion(
                        atencionId,
                        request
                );

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{valoracionId}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    /** Convierte la entidad JPA al DTO de respuesta. */
    private ValoracionTriajeResponse convertirAResponse(
            ValoracionTriaje valoracion
    ) {
        return new ValoracionTriajeResponse(
                valoracion.getId(),
                valoracion.getFechaHora(),
                valoracion.getNivelTriaje(),
                valoracion.getObservaciones(),
                valoracion.getAtencionUrgencias().getId(),
                convertirEnfermeroAReferencia(valoracion.getEnfermero())
        );
    }

    /** Devuelve una referencia sencilla del profesional de Enfermería. */
    private ReferenciaResponse convertirEnfermeroAReferencia(
            Enfermero enfermero
    ) {
        return new ReferenciaResponse(
                enfermero.getId(),
                enfermero.getNombre() + " " + enfermero.getApellidos()
        );
    }
}