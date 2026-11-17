package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un ingreso hospitalario que se ha realizado.
 *
 * La solicitud de ingreso registra la petición; esta clase registra
 * el ingreso efectivo. Sus asignaciones de cama conservan los cambios
 * de ubicación durante la estancia.
 */
@Entity
@Table(name = "ingresos_hospitalarios")
public class IngresoHospitalario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Solicitud de ingreso que dio lugar a este ingreso.
     * Cada solicitud realizada puede producir como máximo un ingreso.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "solicitud_ingreso_id",
            nullable = false,
            unique = true
    )
    private SolicitudIngreso solicitudIngreso;

    // Momento en que comienza el ingreso hospitalario.
    @Column(name = "fecha_hora_ingreso", nullable = false)
    private LocalDateTime fechaHoraIngreso;

    /*
     * Momento en que finaliza el ingreso.
     * Permanece null mientras el paciente siga ingresado.
     */
    @Column(name = "fecha_hora_alta")
    private LocalDateTime fechaHoraAlta;

    /*
     * Forma en que termina el ingreso.
     * Se mantiene null mientras el ingreso siga abierto.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alta")
    private TipoAltaHospitalaria tipoAlta;

    /*
     * Historial de camas asignadas durante el ingreso.
     * No se borran las asignaciones anteriores al cambiar de cama.
     */
    @OneToMany(
            mappedBy = "ingresoHospitalario",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("fechaHoraInicio ASC")
    private List<AsignacionCama> asignacionesCama = new ArrayList<>();

    // Constructor vacío requerido por JPA.
    public IngresoHospitalario() {
    }

    public Long getId() {
        return id;
    }

    public SolicitudIngreso getSolicitudIngreso() {
        return solicitudIngreso;
    }

    public void setSolicitudIngreso(SolicitudIngreso solicitudIngreso) {
        this.solicitudIngreso = solicitudIngreso;
    }

    public LocalDateTime getFechaHoraIngreso() {
        return fechaHoraIngreso;
    }

    public void setFechaHoraIngreso(LocalDateTime fechaHoraIngreso) {
        this.fechaHoraIngreso = fechaHoraIngreso;
    }

    public LocalDateTime getFechaHoraAlta() {
        return fechaHoraAlta;
    }

    public void setFechaHoraAlta(LocalDateTime fechaHoraAlta) {
        this.fechaHoraAlta = fechaHoraAlta;
    }

    public TipoAltaHospitalaria getTipoAlta() {
        return tipoAlta;
    }

    public void setTipoAlta(TipoAltaHospitalaria tipoAlta) {
        this.tipoAlta = tipoAlta;
    }

    public List<AsignacionCama> getAsignacionesCama() {
        return asignacionesCama;
    }

    /**
     * Añade una asignación y establece la relación por ambos lados.
     */
    public void agregarAsignacionCama(AsignacionCama asignacion) {
        if (asignacion != null && !asignacionesCama.contains(asignacion)) {
            asignacionesCama.add(asignacion);
            asignacion.setIngresoHospitalario(this);
        }
    }
}
