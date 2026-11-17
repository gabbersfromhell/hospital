package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registra la solicitud de ingreso de un paciente desde Urgencias.
 *
 * La solicitud no significa que el paciente ya esté ingresado.
 * El ingreso efectivo se representa mediante IngresoHospitalario.
 */
@Entity
@Table(name = "solicitudes_ingreso")
public class SolicitudIngreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atención de Urgencias desde la que se solicita el ingreso.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_urgencias_id", nullable = false)
    private AtencionUrgencias atencionUrgencias;

    // Médico que solicita el ingreso.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_solicitante_id", nullable = false)
    private Medico medicoSolicitante;

    // Servicio hospitalario al que se solicita ingresar al paciente, si se conoce.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_hospitalario_id")
    private ServicioHospitalario servicioHospitalario;

    // Momento en que se registra la solicitud.
    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    // Motivo clínico o administrativo de la petición.
    @Column(nullable = false, length = 500)
    private String motivo;

    /*
     * Estado de la petición.
     * PENDIENTE: todavía no se ha formalizado el ingreso.
     * INGRESO_REALIZADO: ya existe un IngresoHospitalario asociado.
     * CANCELADA: la solicitud ya no sigue adelante.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitudIngreso estado = EstadoSolicitudIngreso.PENDIENTE;

    /*
     * Ingreso efectivo creado a partir de esta solicitud.
     * Puede quedar null mientras no se haya realizado el ingreso.
     */
    @OneToOne(
            mappedBy = "solicitudIngreso",
            fetch = FetchType.LAZY
    )
    private IngresoHospitalario ingresoHospitalario;

    // Constructor vacío requerido por JPA.
    public SolicitudIngreso() {
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

    public Medico getMedicoSolicitante() {
        return medicoSolicitante;
    }

    public void setMedicoSolicitante(Medico medicoSolicitante) {
        this.medicoSolicitante = medicoSolicitante;
    }

    public ServicioHospitalario getServicioHospitalario() {
        return servicioHospitalario;
    }

    public void setServicioHospitalario(
            ServicioHospitalario servicioHospitalario
    ) {
        this.servicioHospitalario = servicioHospitalario;
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

    public EstadoSolicitudIngreso getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitudIngreso estado) {
        this.estado = estado;
    }

    public IngresoHospitalario getIngresoHospitalario() {
        return ingresoHospitalario;
    }
}