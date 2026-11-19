package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos de un contacto, anidado en PacienteRequest o enviado a una ruta del paciente. */
public record ContactoPacienteRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 150) String apellidos,
        @NotBlank @Size(max = 80) String relacionConPaciente,
        @NotBlank @Size(max = 30) String telefono,
        boolean contactoPrincipal
) {
}
