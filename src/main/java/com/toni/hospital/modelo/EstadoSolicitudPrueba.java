package com.toni.hospital.modelo;

/** Estados generales del proceso de una prueba diagnóstica. */
public enum EstadoSolicitudPrueba {
    SOLICITADA,
    EN_PROCESO,
    REALIZADA,
    RESULTADO_VALIDADO,
    CANCELADA
}