package com.toni.hospital.controlador;

import com.toni.hospital.dto.IntervencionQuirurgicaRequest;
import com.toni.hospital.dto.IntervencionQuirurgicaResponse;
import com.toni.hospital.modelo.IntervencionQuirurgica;
import com.toni.hospital.servicio.IntervencionQuirurgicaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/intervenciones-quirurgicas")
public class IntervencionQuirurgicaController {

    private final IntervencionQuirurgicaService intervencionService;

    public IntervencionQuirurgicaController(
            IntervencionQuirurgicaService intervencionService
    ) {
        this.intervencionService = intervencionService;
    }

    @GetMapping
    public List<IntervencionQuirurgicaResponse> listar() {
        return intervencionService.listar().stream()
                .map(this::convertirAResponse).toList();
    }

    @GetMapping("/{id}")
    public IntervencionQuirurgicaResponse buscarPorId(
            @PathVariable Long id
    ) {
        return convertirAResponse(intervencionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<IntervencionQuirurgicaResponse> guardar(
            @Valid @RequestBody IntervencionQuirurgicaRequest request
    ) {
        IntervencionQuirurgica creada =
                intervencionService.guardar(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(ubicacion)
                .body(convertirAResponse(creada));
    }

    @PutMapping("/{id}")
    public IntervencionQuirurgicaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody IntervencionQuirurgicaRequest request
    ) {
        return convertirAResponse(
                intervencionService.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        intervencionService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    private IntervencionQuirurgicaResponse convertirAResponse(
            IntervencionQuirurgica intervencion
    ) {
        return new IntervencionQuirurgicaResponse(
                intervencion.getId(),
                intervencion.getPaciente().getId(),
                intervencion.getHospital().getId(),
                intervencion.getServicioQuirurgico().getId(),
                intervencion.getSalaQuirurgica().getId(),
                intervencion.getSalaQuirurgica().getCodigo(),
                intervencion.getMedicoResponsable().getId(),
                intervencion.getNombreProcedimiento(),
                intervencion.getFechaHoraProgramada(),
                intervencion.getFechaHoraFinProgramada(),
                intervencion.getFechaHoraInicioReal(),
                intervencion.getFechaHoraFinReal(),
                intervencion.getEstado(),
                intervencion.getAtencionUrgencias() == null
                        ? null : intervencion.getAtencionUrgencias().getId(),
                intervencion.getIngresoHospitalario() == null
                        ? null : intervencion.getIngresoHospitalario().getId(),
                intervencion.getCitaAmbulatoria() == null
                        ? null : intervencion.getCitaAmbulatoria().getId(),
                intervencion.getObservaciones()
        );
    }
}
