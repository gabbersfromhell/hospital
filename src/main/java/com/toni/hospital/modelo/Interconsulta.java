package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Solicitud para que otro servicio hospitalario valore al paciente
 * durante una atención de urgencias.
 */
@Entity
@Table(name = "interconsultas")
public class Interconsulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Fecha y hora en que se solicita la valoración.
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    // Motivo por el que se solicita la valoración del otro servicio.
    @Column(nullable = false, length = 500)
    private String motivo;

    /*
     * Estado de la solicitud dentro de nuestro modelo.
     * Los valores están definidos en EstadoInterconsulta.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInterconsulta estado = EstadoInterconsulta.PENDIENTE;

    /*
     * Muchas interconsultas pueden pertenecer a una atención.
     * La clave foránea atencion_urgencias_id se guarda en esta tabla.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_urgencias_id", nullable = false)
    private AtencionUrgencias atencionUrgencias;

    /*
     * Servicio hospitalario al que se dirige esta solicitud.
     * Un servicio puede recibir interconsultas de muchas atenciones.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_hospitalario_id", nullable = false)
    private ServicioHospitalario servicioDestino;

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public Interconsulta() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public EstadoInterconsulta getEstado() {
        return estado;
    }

    public void setEstado(EstadoInterconsulta estado) {
        this.estado = estado;
    }

    public AtencionUrgencias getAtencionUrgencias() {
        return atencionUrgencias;
    }

    public void setAtencionUrgencias(AtencionUrgencias atencionUrgencias) {
        this.atencionUrgencias = atencionUrgencias;
    }

    public ServicioHospitalario getServicioDestino() {
        return servicioDestino;
    }

    public void setServicioDestino(ServicioHospitalario servicioDestino) {
        this.servicioDestino = servicioDestino;
    }
}