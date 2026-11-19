package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Intervención quirúrgica planificada o realizada.
 * Se vincula a urgencias, a un ingreso o a una cita ambulatoria.
 */
@Entity
@Table(name = "intervenciones_quirurgicas")
public class IntervencionQuirurgica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_hospitalario_id", nullable = false)
    private ServicioHospitalario servicioQuirurgico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala_quirurgica_id", nullable = false)
    private SalaQuirurgica salaQuirurgica;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_responsable_id", nullable = false)
    private Medico medicoResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_urgencias_id")
    private AtencionUrgencias atencionUrgencias;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_hospitalario_id")
    private IngresoHospitalario ingresoHospitalario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_ambulatoria_id")
    private CitaAmbulatoria citaAmbulatoria;

    @Column(name = "nombre_procedimiento", nullable = false, length = 200)
    private String nombreProcedimiento;

    @Column(name = "fecha_hora_programada", nullable = false)
    private LocalDateTime fechaHoraProgramada;

    @Column(name = "fecha_hora_fin_programada")
    private LocalDateTime fechaHoraFinProgramada;

    @Column(name = "fecha_hora_inicio_real")
    private LocalDateTime fechaHoraInicioReal;

    @Column(name = "fecha_hora_fin_real")
    private LocalDateTime fechaHoraFinReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoIntervencionQuirurgica estado =
            EstadoIntervencionQuirurgica.PROGRAMADA;

    @Column(length = 1000)
    private String observaciones;

    public IntervencionQuirurgica() {
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

    public ServicioHospitalario getServicioQuirurgico() {
        return servicioQuirurgico;
    }

    public void setServicioQuirurgico(ServicioHospitalario servicioQuirurgico) {
        this.servicioQuirurgico = servicioQuirurgico;
    }

    public SalaQuirurgica getSalaQuirurgica() {
        return salaQuirurgica;
    }

    public void setSalaQuirurgica(SalaQuirurgica salaQuirurgica) {
        this.salaQuirurgica = salaQuirurgica;
    }

    public Medico getMedicoResponsable() {
        return medicoResponsable;
    }

    public void setMedicoResponsable(Medico medicoResponsable) {
        this.medicoResponsable = medicoResponsable;
    }

    public AtencionUrgencias getAtencionUrgencias() {
        return atencionUrgencias;
    }

    public void setAtencionUrgencias(AtencionUrgencias atencionUrgencias) {
        this.atencionUrgencias = atencionUrgencias;
    }

    public IngresoHospitalario getIngresoHospitalario() {
        return ingresoHospitalario;
    }

    public void setIngresoHospitalario(IngresoHospitalario ingresoHospitalario) {
        this.ingresoHospitalario = ingresoHospitalario;
    }

    public CitaAmbulatoria getCitaAmbulatoria() {
        return citaAmbulatoria;
    }

    public void setCitaAmbulatoria(CitaAmbulatoria citaAmbulatoria) {
        this.citaAmbulatoria = citaAmbulatoria;
    }

    public String getNombreProcedimiento() {
        return nombreProcedimiento;
    }

    public void setNombreProcedimiento(String nombreProcedimiento) {
        this.nombreProcedimiento = nombreProcedimiento;
    }

    public LocalDateTime getFechaHoraProgramada() {
        return fechaHoraProgramada;
    }

    public void setFechaHoraProgramada(LocalDateTime fechaHoraProgramada) {
        this.fechaHoraProgramada = fechaHoraProgramada;
    }

    public LocalDateTime getFechaHoraFinProgramada() {
        return fechaHoraFinProgramada;
    }

    public void setFechaHoraFinProgramada(LocalDateTime fechaHoraFinProgramada) {
        this.fechaHoraFinProgramada = fechaHoraFinProgramada;
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

    public EstadoIntervencionQuirurgica getEstado() {
        return estado;
    }

    public void setEstado(EstadoIntervencionQuirurgica estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
