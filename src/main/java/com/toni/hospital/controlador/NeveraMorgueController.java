package com.toni.hospital.controlador;

import com.toni.hospital.dto.NeveraMorgueRequest;
import com.toni.hospital.dto.NeveraMorgueResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.NeveraMorgue;
import com.toni.hospital.servicio.NeveraMorgueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/neveras-morgue")
public class NeveraMorgueController {

    private final NeveraMorgueService neveraMorgueService;

    public NeveraMorgueController(NeveraMorgueService neveraMorgueService) {
        this.neveraMorgueService = neveraMorgueService;
    }

    /** Lista todas las neveras, incluidas las desactivadas. */
    @GetMapping
    public List<NeveraMorgueResponse> listarNeveras() {
        return neveraMorgueService.listarNeveras()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    /** Lista solo las neveras que se pueden utilizar. */
    @GetMapping("/activas")
    public List<NeveraMorgueResponse> listarNeverasActivas() {
        return neveraMorgueService.listarNeverasActivas()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public NeveraMorgueResponse buscarNeveraPorId(@PathVariable Long id) {
        return convertirAResponse(
                neveraMorgueService.buscarNeveraPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<NeveraMorgueResponse> crearNevera(
            @Valid @RequestBody NeveraMorgueRequest request
    ) {
        NeveraMorgue creada = neveraMorgueService.crearNevera(request);

        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();

        return ResponseEntity
                .created(ubicacion)
                .body(convertirAResponse(creada));
    }

    @PutMapping("/{id}")
    public NeveraMorgueResponse actualizarNevera(
            @PathVariable Long id,
            @Valid @RequestBody NeveraMorgueRequest request
    ) {
        NeveraMorgue actualizada =
                neveraMorgueService.actualizarNevera(id, request);

        return convertirAResponse(actualizada);
    }

    /** Desactivación lógica: mantiene la nevera y su historial. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivarNevera(@PathVariable Long id) {
        neveraMorgueService.desactivarNevera(id);
        return ResponseEntity.noContent().build();
    }

    private NeveraMorgueResponse convertirAResponse(NeveraMorgue nevera) {
        Hospital hospital = nevera.getHospital();

        ReferenciaResponse hospitalResponse = new ReferenciaResponse(
                hospital.getId(),
                hospital.getNombre()
        );

        return new NeveraMorgueResponse(
                nevera.getId(),
                nevera.getCodigo(),
                nevera.getUbicacion(),
                nevera.isActiva(),
                neveraMorgueService.estaOcupada(nevera.getId()),
                hospitalResponse
        );
    }
}