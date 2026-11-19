package com.toni.hospital.controlador;

import com.toni.hospital.dto.PlantaRequest;
import com.toni.hospital.dto.PlantaResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Planta;
import com.toni.hospital.servicio.PlantaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/plantas")
public class PlantaController {

    private final PlantaService plantaService;

    public PlantaController(PlantaService plantaService) {
        this.plantaService = plantaService;
    }

    @GetMapping
    public List<PlantaResponse> listarPlantas() {
        return plantaService.listarPlantas()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public PlantaResponse buscarPlantaPorId(@PathVariable Long id) {
        return convertirAResponse(plantaService.buscarPlantaPorId(id));
    }

    @PostMapping
    public ResponseEntity<PlantaResponse> guardarPlanta(
            @Valid @RequestBody PlantaRequest request
    ) {
        Planta guardada = plantaService.guardarPlanta(request);

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
    public PlantaResponse actualizarPlanta(
            @PathVariable Long id,
            @Valid @RequestBody PlantaRequest request
    ) {
        return convertirAResponse(
                plantaService.actualizarPlanta(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPlanta(@PathVariable Long id) {
        plantaService.eliminarPlanta(id);
        return ResponseEntity.noContent().build();
    }

    private PlantaResponse convertirAResponse(Planta planta) {
        ReferenciaResponse hospital = new ReferenciaResponse(
                planta.getHospital().getId(),
                planta.getHospital().getNombre()
        );

        return new PlantaResponse(
                planta.getId(),
                planta.getNumeroPlanta(),
                hospital
        );
    }
}