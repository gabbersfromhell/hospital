package com.toni.hospital.controlador;

import com.toni.hospital.dto.PoblacionRequest;
import com.toni.hospital.dto.PoblacionResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.Poblacion;
import com.toni.hospital.servicio.PoblacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/poblaciones")
public class PoblacionController {

    private final PoblacionService poblacionService;

    public PoblacionController(PoblacionService poblacionService) {
        this.poblacionService = poblacionService;
    }

    @GetMapping
    public List<PoblacionResponse> listarPoblaciones() {
        return poblacionService.listarPoblaciones()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PoblacionResponse buscarPoblacionPorId(@PathVariable Long id) {
        return convertirAResponse(
                poblacionService.buscarPoblacionPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<PoblacionResponse> guardarPoblacion(
            @Valid @RequestBody PoblacionRequest request
    ) {
        Poblacion guardada = poblacionService.guardarPoblacion(request);

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
    public PoblacionResponse actualizarPoblacion(
            @PathVariable Long id,
            @Valid @RequestBody PoblacionRequest request
    ) {
        Poblacion actualizada =
                poblacionService.actualizarPoblacion(id, request);

        return convertirAResponse(actualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPoblacion(@PathVariable Long id) {
        poblacionService.eliminarPoblacion(id);
        return ResponseEntity.noContent().build();
    }

    private PoblacionResponse convertirAResponse(Poblacion poblacion) {
        Hospital hospital = poblacion.getHospitalReferencia();

        ReferenciaResponse hospitalReferencia = hospital == null
                ? null
                : new ReferenciaResponse(hospital.getId(), hospital.getNombre());

        return new PoblacionResponse(
                poblacion.getId(),
                poblacion.getNombre(),
                hospitalReferencia
        );
    }
}