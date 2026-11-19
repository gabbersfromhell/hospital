package com.toni.hospital.modelo;

/**
 * Categoría profesional de quien solicita una prueba diagnóstica.
 *
 * Este enum registra la categoría profesional; no determina
 * por sí solo qué pruebas puede solicitar cada persona.
 */
public enum TipoProfesionalSolicitante {
    MEDICO,
    ENFERMERO,
    MATRONA,
    ODONTOLOGO,
    FISIOTERAPEUTA,
    PSICOLOGO_CLINICO,
    FARMACEUTICO,
    OTRO
}