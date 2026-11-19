package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registra un periodo durante el que se restringe la visibilidad
 * administrativa de un paciente en un hospital.
 *
 * La restricción no elimina el acceso clínico autorizado.
 * Si fechaHoraFin es null, sigue activa.
 */
@Entity
@Table(name = "restricciones_confidencialidad")
public class RestriccionConfidencialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Paciente cuya información debe protegerse durante este periodo.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Hospital en el que se aplica esta restricción.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    /*
     * Categoría general del motivo.
     * Es información sensible: no se incluirá en el censo administrativo.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoRestriccionConfidencialidad motivo;

    // Momento en que se activa la restricción.
    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    /*
     * Momento en que se levanta la restricción.
     * Mientras sea null, la restricción permanece activa.
     */
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    /** Constructor vacío necesario para que JPA cree la entidad. */
    public RestriccionConfidencialidad() {
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public MotivoRestriccionConfidencialidad getMotivo() {
        return motivo;
    }

    public void setMotivo(
            MotivoRestriccionConfidencialidad motivo
    ) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }
}