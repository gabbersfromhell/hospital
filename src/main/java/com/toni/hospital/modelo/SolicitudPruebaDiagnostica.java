package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Solicitud de una prueba diagnóstica concreta o de un panel,
 * como una radiografía de tórax o un hemograma.
 *
 * Puede originarse durante urgencias, un ingreso hospitalario
 * o una cita ambulatoria. El servicio validará que se indique
 * exactamente uno de esos contextos.
 */
@Entity
@Table(name = "solicitudes_pruebas_diagnosticas")
public class SolicitudPruebaDiagnostica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Servicio hospitalario que realizará la prueba. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servicio_destino_id", nullable = false)
    private ServicioHospitalario servicioDestino;

    /** Categoría general: laboratorio, radiología u otra. */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_prueba", nullable = false, length = 30)
    private TipoPruebaDiagnostica tipo;

    /**
     * Nombre concreto de la prueba o del panel solicitado,
     * por ejemplo "Radiografía de tórax" o "Hemograma".
     */
    @Column(name = "nombre_prueba", nullable = false, length = 150)
    private String nombrePrueba;

    /** Momento en que se registra la solicitud. */
    @Column(name = "fecha_hora_solicitud", nullable = false)
    private LocalDateTime fechaHoraSolicitud;

    /** Indicación clínica por la que se solicita la prueba. */
    @Column(name = "indicacion_clinica", nullable = false, length = 500)
    private String indicacionClinica;

    /** Estado general de la solicitud. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoSolicitudPrueba estado =
            EstadoSolicitudPrueba.SOLICITADA;

    /**
     * Categoría profesional de quien solicita la prueba.
     * No determina por sí sola los permisos para solicitarla.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "tipo_profesional_solicitante",
            nullable = false,
            length = 40
    )
    private TipoProfesionalSolicitante tipoProfesionalSolicitante;

    /**
     * Detalle de la profesión cuando se selecciona OTRO.
     * El servicio comprobará que se indique en ese caso.
     */
    @Column(name = "detalle_otro_profesional", length = 150)
    private String detalleOtroProfesional;

    /** Atención de urgencias a la que pertenece, si corresponde. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_urgencias_id")
    private AtencionUrgencias atencionUrgencias;

    /** Ingreso hospitalario al que pertenece, si corresponde. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_hospitalario_id")
    private IngresoHospitalario ingresoHospitalario;

    /** Cita ambulatoria a la que pertenece, si corresponde. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_ambulatoria_id")
    private CitaAmbulatoria citaAmbulatoria;

    /**
     * Resultados de laboratorio asociados a esta solicitud.
     * No se propaga la eliminación para proteger su historial.
     */
    @OneToMany(
            mappedBy = "solicitudPrueba",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("fechaHoraResultado ASC")
    private List<ResultadoLaboratorio> resultadosLaboratorio =
            new ArrayList<>();

    /** Constructor vacío requerido por JPA. */
    public SolicitudPruebaDiagnostica() {
    }

    public Long getId() {
        return id;
    }

    public ServicioHospitalario getServicioDestino() {
        return servicioDestino;
    }

    public void setServicioDestino(ServicioHospitalario servicioDestino) {
        this.servicioDestino = servicioDestino;
    }

    public TipoPruebaDiagnostica getTipo() {
        return tipo;
    }

    public void setTipo(TipoPruebaDiagnostica tipo) {
        this.tipo = tipo;
    }

    public String getNombrePrueba() {
        return nombrePrueba;
    }

    public void setNombrePrueba(String nombrePrueba) {
        this.nombrePrueba = nombrePrueba;
    }

    public LocalDateTime getFechaHoraSolicitud() {
        return fechaHoraSolicitud;
    }

    public void setFechaHoraSolicitud(LocalDateTime fechaHoraSolicitud) {
        this.fechaHoraSolicitud = fechaHoraSolicitud;
    }

    public String getIndicacionClinica() {
        return indicacionClinica;
    }

    public void setIndicacionClinica(String indicacionClinica) {
        this.indicacionClinica = indicacionClinica;
    }

    public EstadoSolicitudPrueba getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitudPrueba estado) {
        this.estado = estado;
    }

    public TipoProfesionalSolicitante getTipoProfesionalSolicitante() {
        return tipoProfesionalSolicitante;
    }

    public void setTipoProfesionalSolicitante(
            TipoProfesionalSolicitante tipoProfesionalSolicitante
    ) {
        this.tipoProfesionalSolicitante = tipoProfesionalSolicitante;
    }

    public String getDetalleOtroProfesional() {
        return detalleOtroProfesional;
    }

    public void setDetalleOtroProfesional(String detalleOtroProfesional) {
        this.detalleOtroProfesional = detalleOtroProfesional;
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

    public List<ResultadoLaboratorio> getResultadosLaboratorio() {
        return resultadosLaboratorio;
    }

    /** Añade un resultado y establece la relación en ambos lados. */
    public void agregarResultadoLaboratorio(
            ResultadoLaboratorio resultado
    ) {
        if (resultado != null && !resultadosLaboratorio.contains(resultado)) {
            resultadosLaboratorio.add(resultado);
            resultado.setSolicitudPrueba(this);
        }
    }
}