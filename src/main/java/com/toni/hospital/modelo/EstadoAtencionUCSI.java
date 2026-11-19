package com.toni.hospital.modelo;

/** Estado del episodio del paciente en la UCSI. */
public enum EstadoAtencionUCSI {
    PROGRAMADA,
    EN_PREPARACION,
    EN_INTERVENCION,
    EN_RECUPERACION,
    ALTA_DOMICILIARIA,
    INGRESO_HOSPITALARIO,
    CANCELADA
}
