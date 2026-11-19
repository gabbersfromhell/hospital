package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Cita de un paciente en consultas externas o rehabilitación.
 *
 * Una cita representa una sesión concreta. Por ejemplo, cada sesión
 * de un tratamiento de rehabilitación tendrá su propio registro.
 */
@Entity
@Table(name = "citas_ambulatorias")
public class CitaAmbulatoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Paciente que tiene la cita. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /** Hospital donde se atiende al paciente. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    /** Servicio que realiza la consulta o sesión. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_hospitalario_id", nullable = false)
    private ServicioHospitalario servicio;

    /** Indica si es consulta externa o rehabilitación. */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cita", nullable = false, length = 30)
    private TipoCitaAmbulatoria tipo;

    /** Fecha y hora previstas para la cita. */
    @Column(name = "fecha_hora_programada", nullable = false)
    private LocalDateTime fechaHoraProgramada;

    /** Momento en que comenzó realmente la atención, si comenzó. */
    @Column(name = "fecha_hora_inicio_real")
    private LocalDateTime fechaHoraInicioReal;

    /** Momento en que terminó realmente la atención, si terminó. */
    @Column(name = "fecha_hora_fin_real")
    private LocalDateTime fechaHoraFinReal;

    /** Estado actual de la cita. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoCitaAmbulatoria estado = EstadoCitaAmbulatoria.PROGRAMADA;

    /** Motivo o indicación de la cita. */
    @Column(length = 500)
    private String motivo;

    /** Notas administrativas u organizativas de la cita. */
    @Column(length = 1000)
    private String observaciones;

    /** Constructor vacío requerido por JPA. */
    public CitaAmbulatoria() {
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

    public ServicioHospitalario getServicio() {
        return servicio;
    }

    public void setServicio(ServicioHospitalario servicio) {
        this.servicio = servicio;
    }

    public TipoCitaAmbulatoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCitaAmbulatoria tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaHoraProgramada() {
        return fechaHoraProgramada;
    }

    public void setFechaHoraProgramada(LocalDateTime fechaHoraProgramada) {
        this.fechaHoraProgramada = fechaHoraProgramada;
    }

    public LocalDateTime getFechaHoraInicioReal() {
        return fechaHoraInicioReal;
    }

    public void setFechaHoraInicioReal(LocalDateTime fechaHoraInicioReal) {
        this.fechaHoraInicioReal = fechaHoraInicioReal;
    }

    public LocalDateTime getFechaHoraFinReal() {
        return fechaHoraFinReal;
    }

    public void setFechaHoraFinReal(LocalDateTime fechaHoraFinReal) {
        this.fechaHoraFinReal = fechaHoraFinReal;
    }

    public EstadoCitaAmbulatoria getEstado() {
        return estado;
    }

    public void setEstado(EstadoCitaAmbulatoria estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}