package com.toni.hospital.controlador;

import com.toni.hospital.dto.HospitalRequest;
import com.toni.hospital.dto.HospitalResponse;
import com.toni.hospital.dto.ReferenciaResponse;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.ServicioHospitalario;
import com.toni.hospital.servicio.HospitalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hospitales")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public List<HospitalResponse> listarHospitales() {
        return hospitalService.listarHospitales()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public HospitalResponse buscarHospitalPorId(@PathVariable Long id) {
        return convertirAResponse(hospitalService.buscarHospitalPorId(id));
    }

    @PostMapping
    public ResponseEntity<HospitalResponse> guardarHospital(
            @Valid @RequestBody HospitalRequest request
    ) {
        Hospital guardado = hospitalService.guardarHospital(request);

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
    public HospitalResponse actualizarHospital(
            @PathVariable Long id,
            @Valid @RequestBody HospitalRequest request
    ) {
        Hospital actualizado =
                hospitalService.actualizarHospital(id, request);

        return convertirAResponse(actualizado);
    }

    private HospitalResponse convertirAResponse(Hospital hospital) {
        List<ReferenciaResponse> servicios = hospital.getServicios()
                .stream()
                .map(this::convertirServicioAReferencia)
                .toList();

        return new HospitalResponse(
                hospital.getId(),
                hospital.getNombre(),
                hospital.getMunicipio(),
                hospital.getDireccion(),
                hospital.getTelefono(),
                servicios
        );
    }

    private ReferenciaResponse convertirServicioAReferencia(
            ServicioHospitalario servicio
    ) {
        return new ReferenciaResponse(
                servicio.getId(),
                servicio.getNombre()
        );
    }
}