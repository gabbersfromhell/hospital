package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registra un traslado de un paciente desde el hospital que lo atiende
 * hacia otro hospital.
 *
 * El traslado queda asociado a una atención de Urgencias. El hospital
 * de origen se obtiene de esa atención y el destino se guarda aquí.
 */
@Entity
@Table(name = "traslados_hospitalarios")
public class TrasladoHospitalario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Atención de Urgencias durante la que se solicita el traslado.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "atencion_urgencias_id", nullable = false)
    private AtencionUrgencias atencionUrgencias;

    // Hospital al que se traslada al paciente.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_destino_id", nullable = false)
    private Hospital hospitalDestino;

    // Servicio que el paciente necesita en el hospital de destino, si se conoce.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_requerido_id")
    private ServicioHospitalario servicioRequerido;

    // Motivo principal por el que se solicita el traslado.
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_traslado", nullable = false)
    private TipoMotivoTraslado motivoTraslado;

    // Medio de transporte previsto para el traslado.
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transporte", nullable = false)
    private TipoTransporteTraslado tipoTransporte;

    // Momento en que se solicita el traslado.
    @Column(name = "fecha_hora_solicitud", nullable = false)
    private LocalDateTime fechaHoraSolicitud;

    // Momento en que el paciente sale del hospital de origen.
    @Column(name = "fecha_hora_salida")
    private LocalDateTime fechaHoraSalida;

    // Momento en que el paciente llega al hospital de destino.
    @Column(name = "fecha_hora_llegada")
    private LocalDateTime fechaHoraLlegada;

    // Información adicional sobre el traslado.
    @Column(length = 500)
    private String observaciones;

    // Constructor vacío requerido por JPA.
    public TrasladoHospitalario() {
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

    public Hospital getHospitalDestino() {
        return hospitalDestino;
    }

    public void setHospitalDestino(Hospital hospitalDestino) {
        this.hospitalDestino = hospitalDestino;
    }

    public ServicioHospitalario getServicioRequerido() {
        return servicioRequerido;
    }

    public void setServicioRequerido(
            ServicioHospitalario servicioRequerido
    ) {
        this.servicioRequerido = servicioRequerido;
    }

    public TipoMotivoTraslado getMotivoTraslado() {
        return motivoTraslado;
    }

    public void setMotivoTraslado(TipoMotivoTraslado motivoTraslado) {
        this.motivoTraslado = motivoTraslado;
    }

    public TipoTransporteTraslado getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(
            TipoTransporteTraslado tipoTransporte
    ) {
        this.tipoTransporte = tipoTransporte;
    }

    public LocalDateTime getFechaHoraSolicitud() {
        return fechaHoraSolicitud;
    }

    public void setFechaHoraSolicitud(LocalDateTime fechaHoraSolicitud) {
        this.fechaHoraSolicitud = fechaHoraSolicitud;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(LocalDateTime fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setFechaHoraLlegada(LocalDateTime fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}