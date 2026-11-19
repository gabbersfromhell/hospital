package com.toni.hospital.dto;

import com.toni.hospital.modelo.ResultadoAtencionUrgencias;
import com.toni.hospital.modelo.TipoCircuitoUrgencias;
import com.toni.hospital.modelo.TipoCobertura;
import com.toni.hospital.modelo.VinculoProfesionalAtencion;
import java.time.LocalDateTime;

/** Vista administrativa de una atención de urgencias. */
public record AtencionUrgenciasResponse(
        Long id, LocalDateTime fechaHoraLlegada,
        LocalDateTime fechaHoraPrimeraAsistenciaMedica,
        LocalDateTime fechaHoraFinUrgencias, String motivoConsulta,
        TipoCircuitoUrgencias circuitoUrgencias, ResultadoAtencionUrgencias resultado,
        ReferenciaResponse hospitalAtencion, ReferenciaResponse ubicacionActual,
        TipoCobertura tipoCobertura, String entidadCobertura,
        PacienteResumenResponse paciente, VinculoProfesionalAtencion vinculoProfesional,
        boolean accidenteLaboral, boolean bajoCustodiaPolicial
) {
}
