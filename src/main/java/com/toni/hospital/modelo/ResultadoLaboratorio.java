package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Resultado de una determinación concreta de laboratorio.
 *
 * Una solicitud puede tener varios resultados, por ejemplo,
 * uno para hemoglobina y otro para glucosa.
 */
@Entity
@Table(name = "resultados_laboratorio")
public class ResultadoLaboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Solicitud de prueba a la que pertenece este resultado. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitud_prueba_id", nullable = false)
    private SolicitudPruebaDiagnostica solicitudPrueba;

    /** Nombre del parámetro, por ejemplo, "Hemoglobina". */
    @Column(name = "nombre_determinacion", nullable = false, length = 120)
    private String nombreDeterminacion;

    /**
     * Resultado numérico, si corresponde.
     * Puede quedar null si el resultado es textual.
     */
    @Column(name = "valor_numerico", precision = 14, scale = 4)
    private BigDecimal valorNumerico;

    /**
     * Resultado textual, por ejemplo, "negativo".
     * También puede contener una observación del laboratorio.
     */
    @Column(name = "valor_textual", length = 2000)
    private String valorTextual;

    /** Unidad correspondiente al valor numérico, si aplica. */
    @Column(name = "unidad_medida", length = 40)
    private String unidadMedida;

    /** Intervalo de referencia informado por el laboratorio, si aplica. */
    @Column(name = "intervalo_referencia", length = 120)
    private String intervaloReferencia;

    /** Fecha y hora en que se registró el resultado. */
    @Column(name = "fecha_hora_resultado", nullable = false)
    private LocalDateTime fechaHoraResultado;

    /** Estado de validación del resultado. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoResultadoLaboratorio estado =
            EstadoResultadoLaboratorio.PENDIENTE_REVISION;

    /** Constructor vacío requerido por JPA. */
    public ResultadoLaboratorio() {
    }

    public Long getId() {
        return id;
    }

    public SolicitudPruebaDiagnostica getSolicitudPrueba() {
        return solicitudPrueba;
    }

    public void setSolicitudPrueba(
            SolicitudPruebaDiagnostica solicitudPrueba
    ) {
        this.solicitudPrueba = solicitudPrueba;
    }

    public String getNombreDeterminacion() {
        return nombreDeterminacion;
    }

    public void setNombreDeterminacion(String nombreDeterminacion) {
        this.nombreDeterminacion = nombreDeterminacion;
    }

    public BigDecimal getValorNumerico() {
        return valorNumerico;
    }

    public void setValorNumerico(BigDecimal valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public String getValorTextual() {
        return valorTextual;
    }

    public void setValorTextual(String valorTextual) {
        this.valorTextual = valorTextual;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getIntervaloReferencia() {
        return intervaloReferencia;
    }

    public void setIntervaloReferencia(String intervaloReferencia) {
        this.intervaloReferencia = intervaloReferencia;
    }

    public LocalDateTime getFechaHoraResultado() {
        return fechaHoraResultado;
    }

    public void setFechaHoraResultado(LocalDateTime fechaHoraResultado) {
        this.fechaHoraResultado = fechaHoraResultado;
    }

    public EstadoResultadoLaboratorio getEstado() {
        return estado;
    }

    public void setEstado(EstadoResultadoLaboratorio estado) {
        this.estado = estado;
    }
}