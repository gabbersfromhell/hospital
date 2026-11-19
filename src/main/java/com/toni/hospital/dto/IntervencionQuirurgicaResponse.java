package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoIntervencionQuirurgica;

import java.time.LocalDateTime;

/** Datos devueltos para una intervención quirúrgica. */
public record IntervencionQuirurgicaResponse(
        Long id,
        Long pacienteId,
        Long hospitalId,
        Long servicioQuirurgicoId,
        Long salaQuirurgicaId,
        String salaCodigo,
        Long medicoResponsableId,
        String nombreProcedimiento,
        LocalDateTime fechaHoraProgramada,
        LocalDateTime fechaHoraFinProgramada,
        LocalDateTime fechaHoraInicioReal,
        LocalDateTime fechaHoraFinReal,
        EstadoIntervencionQuirurgica estado,
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        Long citaAmbulatoriaId,
        String observaciones
) {
}
