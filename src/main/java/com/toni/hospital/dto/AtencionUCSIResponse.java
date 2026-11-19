package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoAtencionUCSI;

import java.time.LocalDateTime;

/** Datos devueltos para un episodio UCSI. */
public record AtencionUCSIResponse(
        Long id,
        Long intervencionQuirurgicaId,
        Long pacienteId,
        Long ingresoHospitalarioPosteriorId,
        EstadoAtencionUCSI estado,
        LocalDateTime fechaHoraEntrada,
        LocalDateTime fechaHoraEntradaQuirofano,
        LocalDateTime fechaHoraSalidaQuirofano,
        LocalDateTime fechaHoraInicioRecuperacion,
        LocalDateTime fechaHoraAlta,
        String observaciones
) {
}
