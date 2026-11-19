// EntradaMorgueRequest.java
package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/** Nevera y momento de entrada del paciente en la morgue. */
public record EntradaMorgueRequest(
        @NotNull @Positive Long neveraMorgueId,
        @NotNull LocalDateTime fechaHoraEntradaMorgue
) {
}