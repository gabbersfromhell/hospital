package com.toni.hospital.dto;

import com.toni.hospital.modelo.MedioLlegadaUrgencias;
import com.toni.hospital.modelo.ProcedenciaUrgencias;
import com.toni.hospital.modelo.ResultadoAtencionUrgencias;
import com.toni.hospital.modelo.TipoCircuitoUrgencias;
import com.toni.hospital.modelo.TipoCobertura;
import com.toni.hospital.modelo.VinculoProfesionalAtencion;

import java.time.LocalDateTime;

/**
 * Datos de una atención de Urgencias que se devuelven al cliente.
 */
public record AtencionUrgenciasResponse(
        Long id,
        LocalDateTime fechaHoraLlegada,
        MedioLlegadaUrgencias medioLlegada,
        ProcedenciaUrgencias procedencia,
        String codigoLlamamiento,
        LocalDateTime fechaHoraPrimeraAsistenciaMedica,
        LocalDateTime fechaHoraFinUrgencias,
        String motivoConsulta,
        TipoCircuitoUrgencias circuitoUrgencias,
        ResultadoAtencionUrgencias resultado,
        ReferenciaResponse hospitalAtencion,
        ReferenciaResponse ubicacionActual,
        TipoCobertura tipoCobertura,
        String entidadCobertura,
        PacienteResumenResponse paciente,
        VinculoProfesionalAtencion vinculoProfesional,
        boolean accidenteLaboral,
        boolean bajoCustodiaPolicial
) {
}