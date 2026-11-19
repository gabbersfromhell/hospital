package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Datos para programar o actualizar una intervención quirúrgica. */
public record IntervencionQuirurgicaRequest(
        @NotNull Long pacienteId,
        @NotNull Long hospitalId,
        @NotNull Long servicioQuirurgicoId,
        @NotNull Long salaQuirurgicaId,
        @NotNull Long medicoResponsableId,
        @NotBlank @Size(max = 200) String nombreProcedimiento,
        @NotNull LocalDateTime fechaHoraProgramada,
        LocalDateTime fechaHoraFinProgramada,
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        Long citaAmbulatoriaId,
        @Size(max = 1000) String observaciones
) {
}
