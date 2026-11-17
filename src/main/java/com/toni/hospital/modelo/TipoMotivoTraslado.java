package com.toni.hospital.modelo;

/**
 * Motivo general por el que se traslada al paciente a otro hospital.
 *
 * Estos valores son para nuestro modelo; no representan códigos internos
 * confirmados del hospital.
 */
public enum TipoMotivoTraslado {

    /** El paciente necesita ingresar en su hospital de referencia. */
    INGRESO_HOSPITAL_REFERENCIA,

    /** El hospital actual no ofrece el servicio que necesita el paciente. */
    SERVICIO_NO_DISPONIBLE,

    /** Otro motivo que se detallará en el traslado. */
    OTRO
}