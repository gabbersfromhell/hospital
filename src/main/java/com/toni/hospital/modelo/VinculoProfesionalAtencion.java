package com.toni.hospital.modelo;

/**
 * Relación profesional del paciente con el hospital o con otro organismo.
 * No determina por sí sola la prioridad clínica del triaje.
 */
public enum VinculoProfesionalAtencion {
    NO_APLICA,
    TRABAJADOR_HOSPITAL,
    POLICIA,
    OTRO_PROFESIONAL
}