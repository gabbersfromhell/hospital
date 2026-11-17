package com.toni.hospital.modelo;

/**
 * Estado operativo de una cama.
 * No representa si tiene un paciente asignado.
 */
public enum EstadoCama {
    OPERATIVA,
    EN_LIMPIEZA,
    FUERA_DE_SERVICIO
}