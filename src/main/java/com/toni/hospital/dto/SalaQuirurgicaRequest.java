package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoSalaQuirurgica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos recibidos para crear o actualizar una sala quirúrgica. */
public record SalaQuirurgicaRequest(
        @NotNull Long hospitalId,
        @NotBlank @Size(max = 30) String codigo,
        @NotBlank @Size(max = 100) String nombre,
        @NotNull TipoSalaQuirurgica tipo,
        @Size(max = 150) String ubicacion
) {
}
