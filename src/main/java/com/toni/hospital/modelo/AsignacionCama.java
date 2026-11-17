package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registra la asignación de una cama a un ingreso hospitalario.
 *
 * Si el paciente cambia de cama, se cierra esta asignación y se crea otra.
 * Así se conserva el historial de ubicaciones durante el ingreso.
 */
@Entity
@Table(name = "asignaciones_cama")
public class AsignacionCama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ingreso hospitalario al que pertenece esta asignación.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ingreso_hospitalario_id", nullable = false)
    private IngresoHospitalario ingresoHospitalario;

    // Cama ocupada durante esta asignación.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cama_id", nullable = false)
    private Cama cama;

    // Momento en que comienza la asignación.
    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    /*
     * Momento en que termina la asignación.
     * Si es null, esta es la cama actual del ingreso.
     */
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    // Motivo del cambio o de la asignación, si se quiere registrar.
    @Column(length = 250)
    private String motivo;

    // Constructor vacío requerido por JPA.
    public AsignacionCama() {
    }

    public Long getId() {
        return id;
    }

    public IngresoHospitalario getIngresoHospitalario() {
        return ingresoHospitalario;
    }

    public void setIngresoHospitalario(IngresoHospitalario ingresoHospitalario) {
        this.ingresoHospitalario = ingresoHospitalario;
    }

    public Cama getCama() {
        return cama;
    }

    public void setCama(Cama cama) {
        this.cama = cama;
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

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}