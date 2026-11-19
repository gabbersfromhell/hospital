package com.toni.hospital.dto;

import com.toni.hospital.modelo.FormaFarmaceutica;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Datos recibidos para añadir un medicamento al catálogo. */
public record MedicamentoRequest(

        @NotBlank
        @Size(max = 150)
        String nombreGenerico,

        @Size(max = 150)
        String nombreComercial,

        @NotBlank
        @Size(max = 250)
        String principiosActivos,

        @NotBlank
        @Size(max = 100)
        String concentracion,

        @NotNull
        FormaFarmaceutica formaFarmaceutica,

        @Size(max = 150)
        String presentacion
) {
}