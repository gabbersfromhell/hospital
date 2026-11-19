package com.toni.hospital.modelo;

/**
 * Motivo general por el que se activa una restricción de confidencialidad.
 *
 * Este dato es sensible y no debe mostrarse en respuestas administrativas
 * generales.
 */
public enum MotivoRestriccionConfidencialidad {
    VIOLENCIA_DE_GENERO,
    VIOLENCIA_SEXUAL,
    RIESGO_DE_SEGURIDAD,
    OTRA_SITUACION_PROTEGIDA
}