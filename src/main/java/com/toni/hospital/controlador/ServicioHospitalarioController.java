package com.toni.hospital.controlador;

import com.toni.hospital.dto.ServicioHospitalarioRequest;
import com.toni.hospital.dto.ServicioHospitalarioResponse;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.servicio.ServicioHospitalarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/servicios-hospitalarios")
public class ServicioHospitalarioController {

    private final ServicioHospitalarioService servicioHospitalarioService;

    public ServicioHospitalarioController(
            ServicioHospitalarioService servicioHospitalarioService
    ) {
        this.servicioHospitalarioService = servicioHospitalarioService;
    }

    @GetMapping
    public List<ServicioHospitalarioResponse> listarServicios() {
        return servicioHospitalarioService.listarServicios()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ServicioHospitalarioResponse buscarServicioPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(
                servicioHospitalarioService.buscarServicioPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<ServicioHospitalarioResponse> guardarServicio(
            @Valid @RequestBody ServicioHospitalarioRequest request
    ) {
        ServicioHospitalario guardado =
                servicioHospitalarioService.guardarServicio(request);

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
    public ServicioHospitalarioResponse actualizarServicio(
            @PathVariable Long id,
            @Valid @RequestBody ServicioHospitalarioRequest request
    ) {
        ServicioHospitalario actualizado =
                servicioHospitalarioService.actualizarServicio(id, request);

        return convertirAResponse(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarServicio(@PathVariable Long id) {
        servicioHospitalarioService.desactivarServicio(id);
        return ResponseEntity.noContent().build();
    }

    private ServicioHospitalarioResponse convertirAResponse(
            ServicioHospitalario servicio
    ) {
        return new ServicioHospitalarioResponse(
                servicio.getId(),
                servicio.getNombre(),
                servicio.getTipoServicio(),
                servicio.isActivo()
        );
    }
}