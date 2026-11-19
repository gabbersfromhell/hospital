package com.toni.hospital.controlador;

import com.toni.hospital.dto.CamaRequest;
import com.toni.hospital.dto.CamaResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Cama;
import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.servicio.CamaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/camas")
public class CamaController {

    private final CamaService camaService;

    public CamaController(CamaService camaService) {
        this.camaService = camaService;
    }

    @GetMapping
    public List<CamaResponse> listarCamas() {
        return camaService.listarCamas()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/disponibles")
    public List<CamaResponse> listarCamasDisponibles() {
        return camaService.listarCamasDisponibles()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public CamaResponse buscarCamaPorId(@PathVariable Long id) {
        return convertirAResponse(camaService.buscarCamaPorId(id));
    }

    @PostMapping
    public ResponseEntity<CamaResponse> guardarCama(
            @Valid @RequestBody CamaRequest request
    ) {
        Cama guardada = camaService.guardarCama(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    @PutMapping("/{id}")
    public CamaResponse actualizarCama(
            @PathVariable Long id,
            @Valid @RequestBody CamaRequest request
    ) {
        return convertirAResponse(camaService.actualizarCama(id, request));
    }

    private CamaResponse convertirAResponse(Cama cama) {
        Habitacion habitacion = cama.getHabitacion();

        ReferenciaResponse habitacionResponse = new ReferenciaResponse(
                habitacion.getId(),
                habitacion.getNumeroHabitacion()
        );

        return new CamaResponse(
                cama.getId(),
                cama.getNumeroCama(),
                cama.getEstado(),
                camaService.estaOcupada(cama.getId()),
                habitacionResponse
        );
    }
}