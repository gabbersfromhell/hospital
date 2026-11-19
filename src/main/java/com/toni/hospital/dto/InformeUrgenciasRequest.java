package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Contenido clínico del informe; la atención se identifica en la ruta. */
public record InformeUrgenciasRequest(
        @NotNull @Positive Long medicoResponsableId,
        @NotBlank @Size(max = 4000) String resumenClinico,
        @Size(max = 1500) String diagnostico,
        @Size(max = 3000) String tratamiento,
        @Size(max = 2000) String recomendaciones
) {
}