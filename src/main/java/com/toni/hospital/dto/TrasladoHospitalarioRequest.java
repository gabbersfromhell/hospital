package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoMotivoTraslado;
import com.toni.hospital.modelo.TipoTransporteTraslado;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Datos para solicitar un traslado desde una atención identificada en la ruta. */
public record TrasladoHospitalarioRequest(
        @NotNull @Positive Long hospitalDestinoId,
        Long servicioRequeridoId,
        @NotNull TipoMotivoTraslado motivoTraslado,
        @NotNull TipoTransporteTraslado tipoTransporte,
        @Size(max = 500) String observaciones
) {
}
