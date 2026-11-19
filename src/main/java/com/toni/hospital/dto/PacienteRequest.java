package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoDocumento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Datos para registrar o actualizar a un paciente. */
public record PacienteRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 150) String apellidos,
        TipoDocumento tipoDocumento,
        @Size(max = 30) String numeroDocumento,
        @Size(max = 30) String cip,
        @Size(max = 30) String numeroSeguridadSocial,
        @Size(max = 250) String direccion,
        @Email @Size(max = 150) String correoElectronico,
        @Positive Long poblacionId,
        List<@Valid ContactoPacienteRequest> contactos
) {
}
