package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Pauta de un medicamento para un paciente en un contexto asistencial.
 *
 * La prescripción registra lo indicado. Cada dosis programada y lo
 * sucedido con ella se registra en AdministracionMedicacion.
 */
@Entity
@Table(name = "prescripciones_medicacion")
public class PrescripcionMedicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Producto del catálogo que se ha prescrito. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    /** Médico que registra la prescripción en este modelo. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_prescriptor_id", nullable = false)
    private Medico medicoPrescriptor;

    /** Cantidad indicada en cada administración, si aplica. */
    @Column(name = "dosis_por_administracion", precision = 12, scale = 4)
    private BigDecimal dosisPorAdministracion;

    /** Unidad correspondiente a la dosis, si aplica. */
    @Column(name = "unidad_dosis", length = 40)
    private String unidadDosis;

    /** Vía indicada para la administración. */
    @Enumerated(EnumType.STRING)
    @Column(name = "via_administracion", nullable = false, length = 30)
    private ViaAdministracion viaAdministracion;

    /**
     * Pauta indicada por el profesional.
     * Aquí se registra la pauta; el sistema no la decide.
     */
    @Column(nullable = false, length = 250)
    private String pauta;

    /** Indica si es una prescripción "si precisa". */
    @Column(name = "si_precisa", nullable = false)
    private boolean siPrecisa;

    /** Indicaciones adicionales para una pauta "si precisa". */
    @Column(name = "indicacion_si_precisa", length = 500)
    private String indicacionSiPrecisa;

    /** Motivo clínico registrado para la prescripción. */
    @Column(name = "indicacion_clinica", length = 500)
    private String indicacionClinica;

    /** Momento en que se registra la prescripción. */
    @Column(name = "fecha_hora_prescripcion", nullable = false)
    private LocalDateTime fechaHoraPrescripcion;

    /** Inicio de vigencia de la pauta. */
    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    /** Fin de la pauta, si se ha establecido. */
    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    /** Estado actual de la prescripción. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoPrescripcionMedicacion estado =
            EstadoPrescripcionMedicacion.ACTIVA;

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
     * Dosis programadas y registros de administración.
     * No se propaga la eliminación para conservar el historial.
     */
    @OneToMany(
            mappedBy = "prescripcionMedicacion",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("fechaHoraProgramada ASC")
    private List<AdministracionMedicacion> administraciones =
            new ArrayList<>();

    /** Constructor vacío requerido por JPA. */
    public PrescripcionMedicacion() {
    }

    public Long getId() {
        return id;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Medico getMedicoPrescriptor() {
        return medicoPrescriptor;
    }

    public void setMedicoPrescriptor(Medico medicoPrescriptor) {
        this.medicoPrescriptor = medicoPrescriptor;
    }

    public BigDecimal getDosisPorAdministracion() {
        return dosisPorAdministracion;
    }

    public void setDosisPorAdministracion(
            BigDecimal dosisPorAdministracion
    ) {
        this.dosisPorAdministracion = dosisPorAdministracion;
    }

    public String getUnidadDosis() {
        return unidadDosis;
    }

    public void setUnidadDosis(String unidadDosis) {
        this.unidadDosis = unidadDosis;
    }

    public ViaAdministracion getViaAdministracion() {
        return viaAdministracion;
    }

    public void setViaAdministracion(ViaAdministracion viaAdministracion) {
        this.viaAdministracion = viaAdministracion;
    }

    public String getPauta() {
        return pauta;
    }

    public void setPauta(String pauta) {
        this.pauta = pauta;
    }

    public boolean isSiPrecisa() {
        return siPrecisa;
    }

    public void setSiPrecisa(boolean siPrecisa) {
        this.siPrecisa = siPrecisa;
    }

    public String getIndicacionSiPrecisa() {
        return indicacionSiPrecisa;
    }

    public void setIndicacionSiPrecisa(String indicacionSiPrecisa) {
        this.indicacionSiPrecisa = indicacionSiPrecisa;
    }

    public String getIndicacionClinica() {
        return indicacionClinica;
    }

    public void setIndicacionClinica(String indicacionClinica) {
        this.indicacionClinica = indicacionClinica;
    }

    public LocalDateTime getFechaHoraPrescripcion() {
        return fechaHoraPrescripcion;
    }

    public void setFechaHoraPrescripcion(
            LocalDateTime fechaHoraPrescripcion
    ) {
        this.fechaHoraPrescripcion = fechaHoraPrescripcion;
    }

    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public EstadoPrescripcionMedicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrescripcionMedicacion estado) {
        this.estado = estado;
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

    public void setIngresoHospitalario(
            IngresoHospitalario ingresoHospitalario
    ) {
        this.ingresoHospitalario = ingresoHospitalario;
    }

    public CitaAmbulatoria getCitaAmbulatoria() {
        return citaAmbulatoria;
    }

    public void setCitaAmbulatoria(CitaAmbulatoria citaAmbulatoria) {
        this.citaAmbulatoria = citaAmbulatoria;
    }

    public List<AdministracionMedicacion> getAdministraciones() {
        return administraciones;
    }

    /** Añade una administración y establece la relación por ambos lados. */
    public void agregarAdministracion(
            AdministracionMedicacion administracion
    ) {
        if (administracion != null
                && !administraciones.contains(administracion)) {
            administraciones.add(administracion);
            administracion.setPrescripcionMedicacion(this);
        }
    }
}