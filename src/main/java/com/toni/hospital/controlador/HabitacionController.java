package com.toni.hospital.controlador;

import com.toni.hospital.dto.HabitacionRequest;
import com.toni.hospital.dto.HabitacionResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Habitacion;
import com.toni.hospital.servicio.HabitacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class HabitacionController {

    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping
    public List<HabitacionResponse> listarHabitaciones() {
        return habitacionService.listarHabitaciones()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public HabitacionResponse buscarHabitacionPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(
                habitacionService.buscarHabitacionPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<HabitacionResponse> guardarHabitacion(
            @Valid @RequestBody HabitacionRequest request
    ) {
        Habitacion guardada =
                habitacionService.guardarHabitacion(request);

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
    public HabitacionResponse actualizarHabitacion(
            @PathVariable Long id,
            @Valid @RequestBody HabitacionRequest request
    ) {
        return convertirAResponse(
                habitacionService.actualizarHabitacion(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabitacion(@PathVariable Long id) {
        habitacionService.eliminarHabitacion(id);
        return ResponseEntity.noContent().build();
    }

    private HabitacionResponse convertirAResponse(Habitacion habitacion) {
        ReferenciaResponse planta = new ReferenciaResponse(
                habitacion.getPlanta().getId(),
                String.valueOf(habitacion.getPlanta().getNumeroPlanta())
        );

        ReferenciaResponse unidad = new ReferenciaResponse(
                habitacion.getUnidadHospitalaria().getId(),
                habitacion.getUnidadHospitalaria().getNombre()
        );

        return new HabitacionResponse(
                habitacion.getId(),
                habitacion.getNumeroHabitacion(),
                planta,
                unidad
        );
    }
}