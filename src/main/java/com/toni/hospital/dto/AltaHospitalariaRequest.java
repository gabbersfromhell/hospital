package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoAltaHospitalaria;
import jakarta.validation.constraints.NotNull;

/** Datos para registrar el tipo de alta de un ingreso hospitalario. */
public record AltaHospitalariaRequest(@NotNull TipoAltaHospitalaria tipoAlta) {
}
