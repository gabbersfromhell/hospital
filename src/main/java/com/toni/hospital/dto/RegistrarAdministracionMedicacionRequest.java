package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoAdministracionMedicacion;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Datos para registrar lo que ocurrió con una dosis programada. */
public record RegistrarAdministracionMedicacionRequest(

        @NotNull
        EstadoAdministracionMedicacion estado,

        @NotNull
        Long enfermeroResponsableId,

        LocalDateTime fechaHoraAdministracion,

        @Positive
        @Digits(integer = 8, fraction = 4)
        BigDecimal dosisAdministrada,

        @Size(max = 40)
        String unidadDosis,

        @Size(max = 500)
        String motivoNoAdministracion,

        @Size(max = 1000)
        String observaciones
) {
}