package com.toni.hospital.controlador;

import com.toni.hospital.dto.InformeUrgenciasRequest;
import com.toni.hospital.dto.InformeUrgenciasResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.InformeUrgencias;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.servicio.InformeUrgenciasService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints de informes clínicos asociados a atenciones de Urgencias.
 */
@RestController
@RequestMapping("/atenciones-urgencias/{atencionId}/informes")
public class InformeUrgenciasController {

    private final InformeUrgenciasService informeService;

    public InformeUrgenciasController(
            InformeUrgenciasService informeService
    ) {
        this.informeService = informeService;
    }

    @GetMapping
    public List<InformeUrgenciasResponse> listarPorAtencion(
            @PathVariable Long atencionId
    ) {
        return informeService.listarPorAtencion(atencionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{informeId}")
    public InformeUrgenciasResponse buscarPorId(
            @PathVariable Long atencionId,
            @PathVariable Long informeId
    ) {
        return convertirAResponse(
                informeService.buscarPorAtencion(atencionId, informeId)
        );
    }

    @PostMapping
    public ResponseEntity<InformeUrgenciasResponse> crearInforme(
            @PathVariable Long atencionId,
            @Valid @RequestBody InformeUrgenciasRequest request
    ) {
        InformeUrgencias creado =
                informeService.crearInforme(atencionId, request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{informeId}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creado));
    }

    private InformeUrgenciasResponse convertirAResponse(
            InformeUrgencias informe
    ) {
        Medico medico = informe.getMedicoResponsable();

        ReferenciaResponse medicoResponse = new ReferenciaResponse(
                medico.getId(),
                medico.getNombre() + " " + medico.getApellidos()
        );

        return new InformeUrgenciasResponse(
                informe.getId(),
                informe.getAtencionUrgencias().getId(),
                medicoResponse,
                informe.getFechaHoraEmision(),
                informe.getResumenClinico(),
                informe.getDiagnostico(),
                informe.getTratamiento(),
                informe.getRecomendaciones()
        );
    }
}