package com.toni.hospital.controlador;

import com.toni.hospital.dto.MedicoRequest;
import com.toni.hospital.dto.MedicoResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.servicio.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    public List<MedicoResponse> listarMedicos() {
        return medicoService.listarMedicos()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/activos")
    public List<MedicoResponse> listarMedicosActivos() {
        return medicoService.listarMedicosActivos()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public MedicoResponse buscarMedicoPorId(@PathVariable Long id) {
        return convertirAResponse(medicoService.buscarMedicoPorId(id));
    }

    @PostMapping
    public ResponseEntity<MedicoResponse> guardarMedico(
            @Valid @RequestBody MedicoRequest request
    ) {
        Medico guardado = medicoService.guardarMedico(request);

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
    public MedicoResponse actualizarMedico(
            @PathVariable Long id,
            @Valid @RequestBody MedicoRequest request
    ) {
        return convertirAResponse(
                medicoService.actualizarMedico(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarMedico(@PathVariable Long id) {
        medicoService.desactivarMedico(id);
        return ResponseEntity.noContent().build();
    }

    private MedicoResponse convertirAResponse(Medico medico) {
        ReferenciaResponse hospital = new ReferenciaResponse(
                medico.getHospital().getId(),
                medico.getHospital().getNombre()
        );

        ReferenciaResponse servicio = new ReferenciaResponse(
                medico.getServicioHospitalario().getId(),
                medico.getServicioHospitalario().getNombre()
        );

        return new MedicoResponse(
                medico.getId(),
                medico.getNombre(),
                medico.getApellidos(),
                medico.getNumeroColegiado(),
                hospital,
                servicio,
                medico.isActivo()
        );
    }
}