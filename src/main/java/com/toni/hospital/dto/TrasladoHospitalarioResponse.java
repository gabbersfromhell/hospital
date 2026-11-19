package com.toni.hospital.dto;

import com.toni.hospital.modelo.TipoMotivoTraslado;
import com.toni.hospital.modelo.TipoTransporteTraslado;
import java.time.LocalDateTime;

/** Traslado con hospital destino y servicio requerido como referencias breves. */
public record TrasladoHospitalarioResponse(
        Long id, Long atencionUrgenciasId, ReferenciaResponse hospitalDestino,
        ReferenciaResponse servicioRequerido, TipoMotivoTraslado motivoTraslado,
        TipoTransporteTraslado tipoTransporte, LocalDateTime fechaHoraSolicitud,
        LocalDateTime fechaHoraSalida, LocalDateTime fechaHoraLlegada, String observaciones
) {
}
