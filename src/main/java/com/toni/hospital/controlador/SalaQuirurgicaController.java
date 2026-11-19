package com.toni.hospital.controlador;

import com.toni.hospital.dto.SalaQuirurgicaRequest;
import com.toni.hospital.dto.SalaQuirurgicaResponse;
import com.toni.hospital.modelo.SalaQuirurgica;
import com.toni.hospital.servicio.SalaQuirurgicaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/salas-quirurgicas")
public class SalaQuirurgicaController {

    private final SalaQuirurgicaService salaService;

    public SalaQuirurgicaController(SalaQuirurgicaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping("/hospital/{hospitalId}")
    public List<SalaQuirurgicaResponse> listarPorHospital(
            @PathVariable Long hospitalId
    ) {
        return salaService.listarPorHospital(hospitalId).stream()
                .map(this::convertirAResponse).toList();
    }

    @GetMapping("/{id}")
    public SalaQuirurgicaResponse buscarPorId(@PathVariable Long id) {
        return convertirAResponse(salaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SalaQuirurgicaResponse> guardar(
            @Valid @RequestBody SalaQuirurgicaRequest request
    ) {
        SalaQuirurgica guardada = salaService.guardar(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(guardada.getId()).toUri();
        return ResponseEntity.created(ubicacion)
                .body(convertirAResponse(guardada));
    }

    @PutMapping("/{id}")
    public SalaQuirurgicaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SalaQuirurgicaRequest request
    ) {
        return convertirAResponse(salaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        salaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    private SalaQuirurgicaResponse convertirAResponse(SalaQuirurgica sala) {
        return new SalaQuirurgicaResponse(
                sala.getId(),
                sala.getHospital().getId(),
                sala.getCodigo(),
                sala.getNombre(),
                sala.getTipo(),
                sala.getUbicacion(),
                sala.isActiva()
        );
    }
}
