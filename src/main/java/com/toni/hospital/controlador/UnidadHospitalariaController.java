package com.toni.hospital.controlador;

import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.dto.UnidadHospitalariaRequest;
import com.toni.hospital.dto.UnidadHospitalariaResponse;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.modelo.UnidadHospitalaria;
import com.toni.hospital.servicio.UnidadHospitalariaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/unidades-hospitalarias")
public class UnidadHospitalariaController {

    private final UnidadHospitalariaService unidadService;

    public UnidadHospitalariaController(
            UnidadHospitalariaService unidadService
    ) {
        this.unidadService = unidadService;
    }

    @GetMapping
    public List<UnidadHospitalariaResponse> listarUnidades() {
        return unidadService.listarUnidades()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public UnidadHospitalariaResponse buscarUnidadPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(unidadService.buscarUnidadPorId(id));
    }

    @PostMapping
    public ResponseEntity<UnidadHospitalariaResponse> guardarUnidad(
            @Valid @RequestBody UnidadHospitalariaRequest request
    ) {
        UnidadHospitalaria guardada = unidadService.guardarUnidad(request);

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
    public UnidadHospitalariaResponse actualizarUnidad(
            @PathVariable Long id,
            @Valid @RequestBody UnidadHospitalariaRequest request
    ) {
        return convertirAResponse(
                unidadService.actualizarUnidad(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarUnidad(@PathVariable Long id) {
        unidadService.desactivarUnidad(id);
        return ResponseEntity.noContent().build();
    }

    private UnidadHospitalariaResponse convertirAResponse(
            UnidadHospitalaria unidad
    ) {
        ReferenciaResponse hospital = new ReferenciaResponse(
                unidad.getHospital().getId(),
                unidad.getHospital().getNombre()
        );

        ServicioHospitalario servicio = unidad.getServicioHospitalario();

        ReferenciaResponse servicioResponse = servicio == null
                ? null
                : new ReferenciaResponse(
                servicio.getId(),
                servicio.getNombre()
        );

        return new UnidadHospitalariaResponse(
                unidad.getId(),
                unidad.getCodigo(),
                unidad.getNombre(),
                hospital,
                servicioResponse,
                unidad.isActiva()
        );
    }
}