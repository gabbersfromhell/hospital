package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registra la emisión de un justificante de asistencia a Urgencias.
 *
 * Las horas de llegada y fin se consultan en AtencionUrgencias,
 * para no duplicar esos datos. El PDF se generará en otra parte
 * de la aplicación.
 */
@Entity
@Table(name = "justificantes_asistencia")
public class JustificanteAsistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atención de Urgencias que acredita el justificante.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_urgencias_id", nullable = false)
    private AtencionUrgencias atencionUrgencias;

    // Fecha y hora en que se emite el justificante.
    @Column(name = "fecha_hora_emision", nullable = false)
    private LocalDateTime fechaHoraEmision;

    // Constructor vacío requerido por JPA.
    public JustificanteAsistencia() {
    }

    public Long getId() {
        return id;
    }

    public AtencionUrgencias getAtencionUrgencias() {
        return atencionUrgencias;
    }

    public void setAtencionUrgencias(AtencionUrgencias atencionUrgencias) {
        this.atencionUrgencias = atencionUrgencias;
    }

    public LocalDateTime getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
    }
}