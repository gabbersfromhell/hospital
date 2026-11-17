package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Guarda una valoración de triaje realizada durante una atención
 * de Urgencias.
 *
 * Una atención puede tener varias valoraciones: la inicial y las
 * reevaluaciones posteriores. Así se conserva el historial de niveles.
 */
@Entity
@Table(name = "valoraciones_triaje")
public class ValoracionTriaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha y hora en que se realiza esta valoración.
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    // Nivel asignado en esta valoración.
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_triaje", nullable = false)
    private NivelTriaje nivelTriaje;

    // Observaciones sobre esta valoración, si se necesitan.
    @Column(length = 500)
    private String observaciones;

    // Atención de Urgencias a la que pertenece esta valoración.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_urgencias_id", nullable = false)
    private AtencionUrgencias atencionUrgencias;

    // Constructor vacío requerido por JPA.
    public ValoracionTriaje() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public NivelTriaje getNivelTriaje() {
        return nivelTriaje;
    }

    public void setNivelTriaje(NivelTriaje nivelTriaje) {
        this.nivelTriaje = nivelTriaje;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public AtencionUrgencias getAtencionUrgencias() {
        return atencionUrgencias;
    }

    public void setAtencionUrgencias(AtencionUrgencias atencionUrgencias) {
        this.atencionUrgencias = atencionUrgencias;
    }
}