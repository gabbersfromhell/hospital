package com.toni.hospital.modelo;

/**
 * Agrupa los distintos tipos de servicios del modelo hospitalario.
 */
public enum TipoServicioHospitalario {

    /** Servicios médicos, como Cardiología o Neurología. */
    MEDICO,

    /** Servicios quirúrgicos, como Traumatología o Cirugía General. */
    QUIRURGICO,

    /** Servicios sociosanitarios, como Convalecencia. */
    SOCIOSANITARIO,

    /** Servicios de apoyo clínico, como Laboratorio o Farmacia. */
    APOYO_CLINICO,

    /** Servicios pertenecientes a la red de salud mental. */
    SALUD_MENTAL
}