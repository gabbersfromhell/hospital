package com.toni.hospital.modelo;

/**
 * Resultado final de una atención de urgencias.
 *
 * Los valores son los que usaremos en el modelo del proyecto.
 */
public enum ResultadoAtencionUrgencias {

    /** El paciente finaliza la atención y se marcha a su domicilio. */
    ALTA_DOMICILIO,

    /** Se realiza un ingreso hospitalario. */
    INGRESO_HOSPITALARIO,

    /** El paciente se traslada a otro centro. */
    TRASLADO_A_OTRO_CENTRO,

    /** La atención finaliza por exitus. */
    EXITUS,

    /** Otro resultado que añadiremos si el proyecto lo necesita. */
    OTRO
}