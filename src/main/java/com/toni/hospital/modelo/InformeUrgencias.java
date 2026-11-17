package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Guarda el contenido de un informe clínico asociado a una atención
 * de Urgencias.
 *
 * Esta entidad almacena los datos del informe; la generación del PDF
 * se realizará en otra parte de la aplicación.
 */
@Entity
@Table(name = "informes_urgencias")
public class InformeUrgencias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atención de Urgencias a la que corresponde el informe.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_urgencias_id", nullable = false)
    private AtencionUrgencias atencionUrgencias;

    // Médico responsable de emitir el informe.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_responsable_id", nullable = false)
    private Medico medicoResponsable;

    // Fecha y hora en que se emite el informe.
    @Column(name = "fecha_hora_emision", nullable = false)
    private LocalDateTime fechaHoraEmision;

    // Resumen de la valoración clínica realizada.
    @Column(name = "resumen_clinico", nullable = false, length = 4000)
    private String resumenClinico;

    // Diagnóstico que consta en este informe.
    @Column(length = 1500)
    private String diagnostico;

    // Tratamiento o indicaciones recogidas en el informe.
    @Column(length = 3000)
    private String tratamiento;

    // Recomendaciones o seguimiento indicado.
    @Column(length = 2000)
    private String recomendaciones;

    // Constructor vacío requerido por JPA.
    public InformeUrgencias() {
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

    public Medico getMedicoResponsable() {
        return medicoResponsable;
    }

    public void setMedicoResponsable(Medico medicoResponsable) {
        this.medicoResponsable = medicoResponsable;
    }

    public LocalDateTime getFechaHoraEmision() {
        return fechaHoraEmision;
    }

    public void setFechaHoraEmision(LocalDateTime fechaHoraEmision) {
        this.fechaHoraEmision = fechaHoraEmision;
    }

    public String getResumenClinico() {
        return resumenClinico;
    }

    public void setResumenClinico(String resumenClinico) {
        this.resumenClinico = resumenClinico;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(String recomendaciones) {
        this.recomendaciones = recomendaciones;
    }
}