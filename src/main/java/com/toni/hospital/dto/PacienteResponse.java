package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoDocumento;
import java.util.List;

/** Respuesta de paciente. Excluye el número de Seguridad Social por minimización de datos. */
public record PacienteResponse(
        Long id, String nombre, String apellidos, TipoDocumento tipoDocumento,
        String numeroDocumento, String cip, String direccion, String correoElectronico,
        ReferenciaResponse poblacion, List<ContactoPacienteResponse> contactos
) {
}
