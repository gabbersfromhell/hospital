package com.toni.hospital.controlador;

import com.toni.hospital.dto.EnfermeroRequest;
import com.toni.hospital.dto.EnfermeroResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Enfermero;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.servicio.EnfermeroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para gestionar enfermeros. */
@RestController
@RequestMapping("/enfermeros")
public class EnfermeroController {

    private final EnfermeroService enfermeroService;

    public EnfermeroController(EnfermeroService enfermeroService) {
        this.enfermeroService = enfermeroService;
    }

    @GetMapping
    public List<EnfermeroResponse> listarEnfermeros() {
        return enfermeroService.listarEnfermeros()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EnfermeroResponse buscarEnfermeroPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(
                enfermeroService.buscarEnfermeroPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<EnfermeroResponse> guardarEnfermero(
            @Valid @RequestBody EnfermeroRequest request
    ) {
        Enfermero guardado = enfermeroService.guardarEnfermero(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardado));
    }

    @PutMapping("/{id}")
    public EnfermeroResponse actualizarEnfermero(
            @PathVariable Long id,
            @Valid @RequestBody EnfermeroRequest request
    ) {
        Enfermero actualizado =
                enfermeroService.actualizarEnfermero(id, request);

        return convertirAResponse(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarEnfermero(
            @PathVariable Long id
    ) {
        enfermeroService.desactivarEnfermero(id);
        return ResponseEntity.noContent().build();
    }

    private EnfermeroResponse convertirAResponse(Enfermero enfermero) {
        Hospital hospital = enfermero.getHospital();

        ReferenciaResponse hospitalResponse = new ReferenciaResponse(
                hospital.getId(),
                hospital.getNombre()
        );

        return new EnfermeroResponse(
                enfermero.getId(),
                enfermero.getNombre(),
                enfermero.getApellidos(),
                enfermero.getNumeroColegiado(),
                hospitalResponse,
                enfermero.isActivo()
        );
    }
}