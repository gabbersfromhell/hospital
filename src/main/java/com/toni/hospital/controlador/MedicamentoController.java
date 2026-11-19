package com.toni.hospital.controlador;

import com.toni.hospital.dto.MedicamentoRequest;
import com.toni.hospital.dto.MedicamentoResponse;
import com.toni.hospital.modelo.Medicamento;
import com.toni.hospital.servicio.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/** Endpoints para gestionar el catálogo de medicamentos. */
@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    /** Lista los medicamentos activos. */
    @GetMapping
    public List<MedicamentoResponse> listarActivos() {
        return medicamentoService.listarActivos()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca medicamentos activos por nombre genérico. */
    @GetMapping("/buscar/nombre")
    public List<MedicamentoResponse> buscarPorNombre(
            @RequestParam String valor
    ) {
        return medicamentoService.buscarPorNombre(valor)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca medicamentos activos por principio activo. */
    @GetMapping("/buscar/principio-activo")
    public List<MedicamentoResponse> buscarPorPrincipioActivo(
            @RequestParam String valor
    ) {
        return medicamentoService.buscarPorPrincipioActivo(valor)
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Busca un medicamento por ID. */
    @GetMapping("/{id}")
    public MedicamentoResponse buscarPorId(@PathVariable Long id) {
        return convertirAResponse(medicamentoService.buscarPorId(id));
    }

    /** Añade un medicamento y responde con 201 Created. */
    @PostMapping
    public ResponseEntity<MedicamentoResponse> guardar(
            @Valid @RequestBody MedicamentoRequest request
    ) {
        Medicamento guardado = medicamentoService.guardar(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(guardado));
    }

    /** Actualiza los datos de un medicamento. */
    @PutMapping("/{id}")
    public MedicamentoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicamentoRequest request
    ) {
        Medicamento actualizado =
                medicamentoService.actualizar(id, request);

        return convertirAResponse(actualizado);
    }

    /** Desactiva el medicamento sin borrarlo de la base de datos. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        medicamentoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    private MedicamentoResponse convertirAResponse(Medicamento medicamento) {
        return new MedicamentoResponse(
                medicamento.getId(),
                medicamento.getNombreGenerico(),
                medicamento.getNombreComercial(),
                medicamento.getPrincipiosActivos(),
                medicamento.getConcentracion(),
                medicamento.getFormaFarmaceutica(),
                medicamento.getPresentacion(),
                medicamento.isActivo()
        );
    }
}