package com.toni.hospital.controlador;

import com.toni.hospital.dto.AtencionUCSIRequest;
import com.toni.hospital.dto.AtencionUCSIResponse;
import com.toni.hospital.dto.CambiarEstadoAtencionUCSIRequest;
import com.toni.hospital.modelo.AtencionUCSI;
import com.toni.hospital.servicio.AtencionUCSIService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/atenciones-ucsi")
public class AtencionUCSIController {

    private final AtencionUCSIService atencionService;

    public AtencionUCSIController(AtencionUCSIService atencionService) {
        this.atencionService = atencionService;
    }

    @GetMapping
    public List<AtencionUCSIResponse> listar() {
        return atencionService.listar().stream()
                .map(this::convertirAResponse).toList();
    }

    @GetMapping("/{id}")
    public AtencionUCSIResponse buscarPorId(@PathVariable Long id) {
        return convertirAResponse(atencionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AtencionUCSIResponse> crear(
            @Valid @RequestBody AtencionUCSIRequest request
    ) {
        AtencionUCSI creada = atencionService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.getId()).toUri();
        return ResponseEntity.created(ubicacion)
                .body(convertirAResponse(creada));
    }

    @PatchMapping("/{id}/estado")
    public AtencionUCSIResponse cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoAtencionUCSIRequest request
    ) {
        return convertirAResponse(
                atencionService.cambiarEstado(id, request)
        );
    }

    private AtencionUCSIResponse convertirAResponse(AtencionUCSI atencion) {
        return new AtencionUCSIResponse(
                atencion.getId(),
                atencion.getIntervencionQuirurgica().getId(),
                atencion.getIntervencionQuirurgica().getPaciente().getId(),
                atencion.getIngresoHospitalarioPosterior() == null
                        ? null
                        : atencion.getIngresoHospitalarioPosterior().getId(),
                atencion.getEstado(),
                atencion.getFechaHoraEntrada(),
                atencion.getFechaHoraEntradaQuirofano(),
                atencion.getFechaHoraSalidaQuirofano(),
                atencion.getFechaHoraInicioRecuperacion(),
                atencion.getFechaHoraAlta(),
                atencion.getObservaciones()
        );
    }
}
