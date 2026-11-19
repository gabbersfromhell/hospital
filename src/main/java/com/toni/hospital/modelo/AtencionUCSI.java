package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Episodio de una persona en la Unidad de Cirugía sin Ingreso.
 * Registra preparación, intervención, recuperación y desenlace.
 */
@Entity
@Table(name = "atenciones_ucsi")
public class AtencionUCSI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "intervencion_quirurgica_id",
            nullable = false,
            unique = true
    )
    private IntervencionQuirurgica intervencionQuirurgica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_hospitalario_posterior_id")
    private IngresoHospitalario ingresoHospitalarioPosterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EstadoAtencionUCSI estado = EstadoAtencionUCSI.PROGRAMADA;

    @Column(name = "fecha_hora_entrada")
    private LocalDateTime fechaHoraEntrada;

    @Column(name = "fecha_hora_entrada_quirofano")
    private LocalDateTime fechaHoraEntradaQuirofano;

    @Column(name = "fecha_hora_salida_quirofano")
    private LocalDateTime fechaHoraSalidaQuirofano;

    @Column(name = "fecha_hora_inicio_recuperacion")
    private LocalDateTime fechaHoraInicioRecuperacion;

    @Column(name = "fecha_hora_alta")
    private LocalDateTime fechaHoraAlta;

    @Column(length = 500)
    private String observaciones;

    public AtencionUCSI() {
    }

    public Long getId() {
        return id;
    }

    public IntervencionQuirurgica getIntervencionQuirurgica() {
        return intervencionQuirurgica;
    }

    public void setIntervencionQuirurgica(
            IntervencionQuirurgica intervencionQuirurgica
    ) {
        this.intervencionQuirurgica = intervencionQuirurgica;
    }

    public IngresoHospitalario getIngresoHospitalarioPosterior() {
        return ingresoHospitalarioPosterior;
    }

    public void setIngresoHospitalarioPosterior(
            IngresoHospitalario ingresoHospitalarioPosterior
    ) {
        this.ingresoHospitalarioPosterior = ingresoHospitalarioPosterior;
    }

    public EstadoAtencionUCSI getEstado() {
        return estado;
    }

    public void setEstado(EstadoAtencionUCSI estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public void setFechaHoraEntrada(LocalDateTime fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public LocalDateTime getFechaHoraEntradaQuirofano() {
        return fechaHoraEntradaQuirofano;
    }

    public void setFechaHoraEntradaQuirofano(
            LocalDateTime fechaHoraEntradaQuirofano
    ) {
        this.fechaHoraEntradaQuirofano = fechaHoraEntradaQuirofano;
    }

    public LocalDateTime getFechaHoraSalidaQuirofano() {
        return fechaHoraSalidaQuirofano;
    }

    public void setFechaHoraSalidaQuirofano(
            LocalDateTime fechaHoraSalidaQuirofano
    ) {
        this.fechaHoraSalidaQuirofano = fechaHoraSalidaQuirofano;
    }

    public LocalDateTime getFechaHoraInicioRecuperacion() {
        return fechaHoraInicioRecuperacion;
    }

    public void setFechaHoraInicioRecuperacion(
            LocalDateTime fechaHoraInicioRecuperacion
    ) {
        this.fechaHoraInicioRecuperacion = fechaHoraInicioRecuperacion;
    }

    public LocalDateTime getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(LocalDateTime fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
