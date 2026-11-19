package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoCitaAmbulatoria;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Datos que se reciben para programar una cita ambulatoria.
 */
public record CitaAmbulatoriaRequest(

        @NotNull
        Long pacienteId,

        @NotNull
        Long hospitalId,

        @NotNull
        Long servicioId,

        @NotNull
        TipoCitaAmbulatoria tipo,

        @NotNull
        LocalDateTime fechaHoraProgramada,

        @Size(max = 500)
        String motivo,

        @Size(max = 1000)
        String observaciones
) {
}