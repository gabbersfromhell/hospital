package com.toni.hospital.controlador;

import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.TrasladoHospitalarioRequest;
import com.toni.hospital.dto.TrasladoHospitalarioResponse;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.modelo.TrasladoHospitalario;
import com.toni.hospital.servicio.TrasladoHospitalarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/atenciones-urgencias/{atencionId}/traslados")
public class TrasladoHospitalarioController {

    private final TrasladoHospitalarioService trasladoService;

    public TrasladoHospitalarioController(
            TrasladoHospitalarioService trasladoService
    ) {
        this.trasladoService = trasladoService;
    }

    @GetMapping
    public List<TrasladoHospitalarioResponse> listarPorAtencion(
            @PathVariable Long atencionId
    ) {
        return trasladoService.listarPorAtencion(atencionId)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{trasladoId}")
    public TrasladoHospitalarioResponse buscarPorId(
            @PathVariable Long atencionId,
            @PathVariable Long trasladoId
    ) {
        return convertirAResponse(
                trasladoService.buscarPorAtencion(atencionId, trasladoId)
        );
    }

    @PostMapping
    public ResponseEntity<TrasladoHospitalarioResponse> solicitarTraslado(
            @PathVariable Long atencionId,
            @Valid @RequestBody TrasladoHospitalarioRequest request
    ) {
        TrasladoHospitalario creado =
                trasladoService.solicitarTraslado(atencionId, request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{trasladoId}")
                .buildAndExpand(creado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creado));
    }

    @PostMapping("/{trasladoId}/salida")
    public TrasladoHospitalarioResponse marcarSalida(
            @PathVariable Long atencionId,
            @PathVariable Long trasladoId
    ) {
        return convertirAResponse(
                trasladoService.marcarSalida(atencionId, trasladoId)
        );
    }

    @PostMapping("/{trasladoId}/llegada")
    public TrasladoHospitalarioResponse marcarLlegada(
            @PathVariable Long atencionId,
            @PathVariable Long trasladoId
    ) {
        return convertirAResponse(
                trasladoService.marcarLlegada(atencionId, trasladoId)
        );
    }

    private TrasladoHospitalarioResponse convertirAResponse(
            TrasladoHospitalario traslado
    ) {
        Hospital destino = traslado.getHospitalDestino();
        ReferenciaResponse hospitalResponse = new ReferenciaResponse(
                destino.getId(),
                destino.getNombre()
        );

        ServicioHospitalario servicio =
                traslado.getServicioRequerido();

        ReferenciaResponse servicioResponse = servicio == null
                ? null
                : new ReferenciaResponse(
                servicio.getId(),
                servicio.getNombre()
        );

        return new TrasladoHospitalarioResponse(
                traslado.getId(),
                traslado.getAtencionUrgencias().getId(),
                hospitalResponse,
                servicioResponse,
                traslado.getMotivoTraslado(),
                traslado.getTipoTransporte(),
                traslado.getFechaHoraSolicitud(),
                traslado.getFechaHoraSalida(),
                traslado.getFechaHoraLlegada(),
                traslado.getObservaciones()
        );
    }
}