// AvisoFunerariaRequest.java
package com.toni.hospital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/** Datos del aviso realizado a la funeraria. */
public record AvisoFunerariaRequest(
        @NotBlank @Size(max = 150) String nombreFuneraria,
        @NotNull LocalDateTime fechaHoraAvisoFuneraria
) {
}