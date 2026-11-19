package com.toni.hospital.dto;

/** Referencia breve a otra entidad, sin serializar toda la relación JPA. */
public record ReferenciaResponse(Long id, String nombre) {
}
