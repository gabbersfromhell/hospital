package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registro de una dosis programada dentro de una prescripción.
 *
 * Indica si se administró, se omitió o fue rechazada por el paciente.
 */
@Entity
@Table(name = "administraciones_medicacion")
public class AdministracionMedicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Prescripción a la que pertenece esta administración. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prescripcion_medicacion_id", nullable = false)
    private PrescripcionMedicacion prescripcionMedicacion;

    /** Momento previsto para administrar la dosis. */
    @Column(name = "fecha_hora_programada", nullable = false)
    private LocalDateTime fechaHoraProgramada;

    /**
     * Momento real en que se administró la dosis.
     * Queda null si se omitió o fue rechazada.
     */
    @Column(name = "fecha_hora_administracion")
    private LocalDateTime fechaHoraAdministracion;

    /**
     * Momento en que se registró el resultado de la dosis,
     * tanto si se administró como si no.
     */
    @Column(name = "fecha_hora_registro_resultado")
    private LocalDateTime fechaHoraRegistroResultado;

    /** Estado de esta administración. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EstadoAdministracionMedicacion estado =
            EstadoAdministracionMedicacion.PROGRAMADA;

    /** Cantidad realmente administrada, si se administró. */
    @Column(name = "dosis_administrada", precision = 12, scale = 4)
    private BigDecimal dosisAdministrada;

    /** Unidad de la cantidad administrada, si corresponde. */
    @Column(name = "unidad_dosis", length = 40)
    private String unidadDosis;

    /**
     * Enfermero/a que registró el resultado de la dosis.
     * Se asigna al pasarla de PROGRAMADA a otro estado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermero_responsable_id")
    private Enfermero enfermeroResponsable;

    /** Motivo registrado si la dosis no se administró. */
    @Column(name = "motivo_no_administracion", length = 500)
    private String motivoNoAdministracion;

    /** Observaciones adicionales sobre la administración. */
    @Column(length = 1000)
    private String observaciones;

    /** Constructor vacío requerido por JPA. */
    public AdministracionMedicacion() {
    }

    public Long getId() {
        return id;
    }

    public PrescripcionMedicacion getPrescripcionMedicacion() {
        return prescripcionMedicacion;
    }

    public void setPrescripcionMedicacion(
            PrescripcionMedicacion prescripcionMedicacion
    ) {
        this.prescripcionMedicacion = prescripcionMedicacion;
    }

    public LocalDateTime getFechaHoraProgramada() {
        return fechaHoraProgramada;
    }

    public void setFechaHoraProgramada(
            LocalDateTime fechaHoraProgramada
    ) {
        this.fechaHoraProgramada = fechaHoraProgramada;
    }

    public LocalDateTime getFechaHoraAdministracion() {
        return fechaHoraAdministracion;
    }

    public void setFechaHoraAdministracion(
            LocalDateTime fechaHoraAdministracion
    ) {
        this.fechaHoraAdministracion = fechaHoraAdministracion;
    }

    public LocalDateTime getFechaHoraRegistroResultado() {
        return fechaHoraRegistroResultado;
    }

    public void setFechaHoraRegistroResultado(
            LocalDateTime fechaHoraRegistroResultado
    ) {
        this.fechaHoraRegistroResultado = fechaHoraRegistroResultado;
    }

    public EstadoAdministracionMedicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoAdministracionMedicacion estado) {
        this.estado = estado;
    }

    public BigDecimal getDosisAdministrada() {
        return dosisAdministrada;
    }

    public void setDosisAdministrada(BigDecimal dosisAdministrada) {
        this.dosisAdministrada = dosisAdministrada;
    }

    public String getUnidadDosis() {
        return unidadDosis;
    }

    public void setUnidadDosis(String unidadDosis) {
        this.unidadDosis = unidadDosis;
    }

    public Enfermero getEnfermeroResponsable() {
        return enfermeroResponsable;
    }

    public void setEnfermeroResponsable(Enfermero enfermeroResponsable) {
        this.enfermeroResponsable = enfermeroResponsable;
    }

    public String getMotivoNoAdministracion() {
        return motivoNoAdministracion;
    }

    public void setMotivoNoAdministracion(
            String motivoNoAdministracion
    ) {
        this.motivoNoAdministracion = motivoNoAdministracion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}