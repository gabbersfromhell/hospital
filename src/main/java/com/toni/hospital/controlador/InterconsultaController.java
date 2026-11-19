package com.toni.hospital.controlador;

import com.toni.hospital.dto.CambiarEstadoInterconsultaRequest;
import com.toni.hospital.dto.InterconsultaRequest;
import com.toni.hospital.dto.InterconsultaResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Interconsulta;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.servicio.InterconsultaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoints para las interconsultas de una atención de Urgencias.
 */
@RestController
@RequestMapping("/atenciones-urgencias/{atencionId}/interconsultas")
public class InterconsultaController {

    private final InterconsultaService interconsultaService;

    public InterconsultaController(
            InterconsultaService interconsultaService
    ) {
        this.interconsultaService = interconsultaService;
    }

    /**
     * GET /atenciones-urgencias/{atencionId}/interconsultas
     */
    @GetMapping
    public List<InterconsultaResponse> listarPorAtencion(
            @PathVariable Long atencionId
    ) {
        return interconsultaService.listarPorAtencion(atencionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /**
     * GET /atenciones-urgencias/{atencionId}/interconsultas/{id}
     */
    @GetMapping("/{id}")
    public InterconsultaResponse buscarPorId(
            @PathVariable Long atencionId,
            @PathVariable Long id
    ) {
        return convertirAResponse(
                interconsultaService.buscarPorAtencion(atencionId, id)
        );
    }

    /**
     * POST /atenciones-urgencias/{atencionId}/interconsultas
     */
    @PostMapping
    public ResponseEntity<InterconsultaResponse> crearInterconsulta(
            @PathVariable Long atencionId,
            @Valid @RequestBody InterconsultaRequest request
    ) {
        Interconsulta creada =
                interconsultaService.crearInterconsulta(atencionId, request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creada));
    }

    /**
     * PUT /atenciones-urgencias/{atencionId}/interconsultas/{id}/estado
     */
    @PutMapping("/{id}/estado")
    public InterconsultaResponse cambiarEstado(
            @PathVariable Long atencionId,
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoInterconsultaRequest request
    ) {
        Interconsulta actualizada = interconsultaService.cambiarEstado(
                atencionId,
                id,
                request
        );

        return convertirAResponse(actualizada);
    }

    private InterconsultaResponse convertirAResponse(
            Interconsulta interconsulta
    ) {
        ServicioHospitalario servicio = interconsulta.getServicioDestino();

        ReferenciaResponse servicioResponse = new ReferenciaResponse(
                servicio.getId(),
                servicio.getNombre()
        );

        return new InterconsultaResponse(
                interconsulta.getId(),
                interconsulta.getFechaSolicitud(),
                interconsulta.getMotivo(),
                interconsulta.getEstado(),
                interconsulta.getAtencionUrgencias().getId(),
                servicioResponse
        );
    }
}