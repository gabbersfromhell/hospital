package com.toni.hospital.dto;

/** Datos de contacto que se muestran junto al paciente. */
public record ContactoPacienteResponse(
        Long id, String nombre, String apellidos, String relacionConPaciente,
        String telefono, boolean contactoPrincipal
) {
}
