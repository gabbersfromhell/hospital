package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Informe de una prueba de diagnóstico por imagen.
 *
 * Guarda los hallazgos y la conclusión del estudio.
 * En el servicio comprobaremos que la solicitud asociada
 * sea de tipo RADIOLOGIA.
 */
@Entity
@Table(name = "informes_radiologicos")
public class InformeRadiologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Solicitud de radiología a la que corresponde este informe. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitud_prueba_id", nullable = false)
    private SolicitudPruebaDiagnostica solicitudPrueba;

    /** Médico responsable de elaborar el informe. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_informante_id", nullable = false)
    private Medico medicoInformante;

    /** Momento en que se realizó el estudio de imagen. */
    @Column(name = "fecha_hora_estudio", nullable = false)
    private LocalDateTime fechaHoraEstudio;

    /** Momento en que se redactó o registró este informe. */
    @Column(name = "fecha_hora_informe", nullable = false)
    private LocalDateTime fechaHoraInforme;

    /** Descripción de los hallazgos observados. */
    @Lob
    @Column(nullable = false)
    private String hallazgos;

    /** Resumen o conclusión del informe. */
    @Lob
    @Column(nullable = false)
    private String conclusion;

    /** Estado actual del informe. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoInformeRadiologico estado =
            EstadoInformeRadiologico.PENDIENTE_VALIDACION;

    /** Constructor vacío requerido por JPA. */
    public InformeRadiologico() {
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

    public Medico getMedicoInformante() {
        return medicoInformante;
    }

    public void setMedicoInformante(Medico medicoInformante) {
        this.medicoInformante = medicoInformante;
    }

    public LocalDateTime getFechaHoraEstudio() {
        return fechaHoraEstudio;
    }

    public void setFechaHoraEstudio(LocalDateTime fechaHoraEstudio) {
        this.fechaHoraEstudio = fechaHoraEstudio;
    }

    public LocalDateTime getFechaHoraInforme() {
        return fechaHoraInforme;
    }

    public void setFechaHoraInforme(LocalDateTime fechaHoraInforme) {
        this.fechaHoraInforme = fechaHoraInforme;
    }

    public String getHallazgos() {
        return hallazgos;
    }

    public void setHallazgos(String hallazgos) {
        this.hallazgos = hallazgos;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public EstadoInformeRadiologico getEstado() {
        return estado;
    }

    public void setEstado(EstadoInformeRadiologico estado) {
        this.estado = estado;
    }
}