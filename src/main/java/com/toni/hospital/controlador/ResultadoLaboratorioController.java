package com.toni.hospital.controlador;

import com.toni.hospital.dto.ResultadoLaboratorioRequest;
import com.toni.hospital.dto.ResultadoLaboratorioResponse;
import com.toni.hospital.modelo.ResultadoLaboratorio;
import com.toni.hospital.servicio.ResultadoLaboratorioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para consultar y registrar resultados de laboratorio. */
@RestController
@RequestMapping("/resultados-laboratorio")
public class ResultadoLaboratorioController {

    private final ResultadoLaboratorioService resultadoService;

    public ResultadoLaboratorioController(
            ResultadoLaboratorioService resultadoService
    ) {
        this.resultadoService = resultadoService;
    }

    /** Lista los resultados de una solicitud diagnóstica. */
    @GetMapping("/solicitud/{solicitudId}")
    public List<ResultadoLaboratorioResponse> listarPorSolicitud(
            @PathVariable Long solicitudId
    ) {
        return resultadoService.listarPorSolicitud(solicitudId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca un resultado por su ID. */
    @GetMapping("/{id}")
    public ResultadoLaboratorioResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(resultadoService.buscarPorId(id));
    }

    /** Registra un resultado pendiente de revisión. */
    @PostMapping
    public ResponseEntity<ResultadoLaboratorioResponse> guardar(
            @Valid @RequestBody ResultadoLaboratorioRequest request
    ) {
        ResultadoLaboratorio guardado = resultadoService.guardar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardado));
    }

    private ResultadoLaboratorioResponse convertirAResponse(
            ResultadoLaboratorio resultado
    ) {
        return new ResultadoLaboratorioResponse(
                resultado.getId(),
                resultado.getSolicitudPrueba().getId(),
                resultado.getNombreDeterminacion(),
                resultado.getValorNumerico(),
                resultado.getValorTextual(),
                resultado.getUnidadMedida(),
                resultado.getIntervaloReferencia(),
                resultado.getFechaHoraResultado(),
                resultado.getEstado()
        );
    }
}