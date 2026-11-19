package com.toni.hospital.dto;

import com.toni.hospital.modelo.EstadoSolicitudPrueba;
import com.toni.hospital.modelo.TipoProfesionalSolicitante;
import com.toni.hospital.modelo.TipoPruebaDiagnostica;

import java.time.LocalDateTime;

/** Datos devueltos al consultar una solicitud de prueba diagnóstica. */
public record SolicitudPruebaDiagnosticaResponse(
        Long id,
        ReferenciaResponse servicioDestino,
        TipoPruebaDiagnostica tipo,
        String nombrePrueba,
        LocalDateTime fechaHoraSolicitud,
        String indicacionClinica,
        EstadoSolicitudPrueba estado,
        TipoProfesionalSolicitante tipoProfesionalSolicitante,
        String detalleOtroProfesional,
        Long atencionUrgenciasId,
        Long ingresoHospitalarioId,
        Long citaAmbulatoriaId
) {
}