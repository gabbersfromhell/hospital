// RetiradaMorgueRequest.java
package com.toni.hospital.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/** Momento en que la funeraria retira al paciente. */
public record RetiradaMorgueRequest(
        @NotNull LocalDateTime fechaHoraRetirada
) {
}