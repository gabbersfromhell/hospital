package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una atención concreta de un paciente en Urgencias.
 *
 * Un paciente puede tener muchas atenciones a lo largo del tiempo.
 * El hospital que atiende esta visita puede ser distinto del hospital
 * de referencia de la población del paciente.
 */
@Entity
@Table(name = "atenciones_urgencias")
public class AtencionUrgencias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Momento en que el paciente llega a Urgencias.
    @Column(name = "fecha_hora_llegada", nullable = false)
    private LocalDateTime fechaHoraLlegada;

    // Medio por el que llega: ambulancia, por sus medios u otro.
    @Enumerated(EnumType.STRING)
    @Column(name = "medio_llegada", nullable = false)
    private MedioLlegadaUrgencias medioLlegada;

    /*
     * Lugar desde el que procede el paciente, si se conoce.
     * Por ejemplo, su domicilio, la vía pública o un centro sanitario.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "procedencia")
    private ProcedenciaUrgencias procedencia;

    /*
     * Código que se muestra en las pantallas para llamar al paciente.
     * Es distinto del ID de esta atención y del CIP del paciente.
     */
    @Column(name = "codigo_llamamiento", length = 30)
    private String codigoLlamamiento;

    /*
     * Momento en que comienza la primera valoración médica.
     * Puede quedar null mientras el paciente no haya sido valorado.
     */
    @Column(name = "fecha_hora_primera_asistencia_medica")
    private LocalDateTime fechaHoraPrimeraAsistenciaMedica;

    /*
     * Momento en que termina la atención en Urgencias.
     * No necesariamente coincide con la salida del hospital:
     * el paciente podría ingresar o trasladarse a otro centro.
     */
    @Column(name = "fecha_hora_fin_urgencias")
    private LocalDateTime fechaHoraFinUrgencias;

    // Motivo que se indica al llegar a Urgencias.
    @Column(name = "motivo_consulta", nullable = false, length = 500)
    private String motivoConsulta;

    /*
     * Circuito asistencial inicial, por ejemplo general, trauma
     * o pediatría. Es independiente del nivel de triaje.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "circuito_urgencias", nullable = false)
    private TipoCircuitoUrgencias circuitoUrgencias;

    /*
     * Resultado una vez finalizada la atención.
     * Puede quedar null mientras la atención continúe abierta.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "resultado_atencion")
    private ResultadoAtencionUrgencias resultado;

    /*
     * Hospital donde se está atendiendo esta visita.
     * Puede no ser el hospital de referencia del paciente.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_atencion_id", nullable = false)
    private Hospital hospitalAtencion;

    /*
     * Ubicación actual del paciente dentro de Urgencias,
     * por ejemplo BOX-07. Puede ser null si aún no se le ha asignado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_actual_id")
    private UbicacionUrgencias ubicacionActual;

    /*
     * Cobertura asociada a esta atención.
     * Puede quedar null mientras no se conozca.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cobertura")
    private TipoCobertura tipoCobertura;

    /*
     * Entidad que cubre la atención, si corresponde.
     * Por ejemplo, una mutua laboral o una aseguradora.
     */
    @Column(name = "entidad_cobertura", length = 150)
    private String entidadCobertura;

    // Paciente atendido. Un paciente puede tener muchas atenciones.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    /*
     * Vínculo profesional del paciente con el hospital
     * u otro organismo. No determina por sí solo el nivel de triaje.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "vinculo_profesional", nullable = false)
    private VinculoProfesionalAtencion vinculoProfesional =
            VinculoProfesionalAtencion.NO_APLICA;

    // Indica si la atención está relacionada con un accidente laboral.
    @Column(name = "accidente_laboral", nullable = false)
    private boolean accidenteLaboral = false;

    /*
     * Indica si el paciente se encuentra bajo custodia policial.
     * No indica que el paciente sea policía.
     */
    @Column(name = "bajo_custodia_policial", nullable = false)
    private boolean bajoCustodiaPolicial = false;

    /*
     * Valoraciones de triaje realizadas durante esta atención.
     * Se conserva el historial de valoraciones y reevaluaciones.
     */
    @OneToMany(
            mappedBy = "atencionUrgencias",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("fechaHora ASC")
    private List<ValoracionTriaje> valoracionesTriaje = new ArrayList<>();

    // Interconsultas solicitadas durante esta atención.
    @OneToMany(
            mappedBy = "atencionUrgencias",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("fechaSolicitud ASC")
    private List<Interconsulta> interconsultas = new ArrayList<>();

    // Solicitudes de ingreso generadas durante esta atención.
    @OneToMany(
            mappedBy = "atencionUrgencias",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @OrderBy("fechaSolicitud ASC")
    private List<SolicitudIngreso> solicitudesIngreso = new ArrayList<>();

    /*
     * Traslados solicitados durante esta atención.
     * Se conservan para consultar el historial de traslados.
     */
    @OneToMany(
            mappedBy = "atencionUrgencias",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<TrasladoHospitalario> trasladosHospitalarios =
            new ArrayList<>();

    // Constructor vacío requerido por JPA.
    public AtencionUrgencias() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaHoraLlegada() {
        return fechaHoraLlegada;
    }

    public void setFechaHoraLlegada(LocalDateTime fechaHoraLlegada) {
        this.fechaHoraLlegada = fechaHoraLlegada;
    }

    public MedioLlegadaUrgencias getMedioLlegada() {
        return medioLlegada;
    }

    public void setMedioLlegada(MedioLlegadaUrgencias medioLlegada) {
        this.medioLlegada = medioLlegada;
    }

    public ProcedenciaUrgencias getProcedencia() {
        return procedencia;
    }

    public void setProcedencia(ProcedenciaUrgencias procedencia) {
        this.procedencia = procedencia;
    }

    public String getCodigoLlamamiento() {
        return codigoLlamamiento;
    }

    public void setCodigoLlamamiento(String codigoLlamamiento) {
        this.codigoLlamamiento = codigoLlamamiento;
    }

    public LocalDateTime getFechaHoraPrimeraAsistenciaMedica() {
        return fechaHoraPrimeraAsistenciaMedica;
    }

    public void setFechaHoraPrimeraAsistenciaMedica(
            LocalDateTime fechaHoraPrimeraAsistenciaMedica
    ) {
        this.fechaHoraPrimeraAsistenciaMedica =
                fechaHoraPrimeraAsistenciaMedica;
    }

    public LocalDateTime getFechaHoraFinUrgencias() {
        return fechaHoraFinUrgencias;
    }

    public void setFechaHoraFinUrgencias(
            LocalDateTime fechaHoraFinUrgencias
    ) {
        this.fechaHoraFinUrgencias = fechaHoraFinUrgencias;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public TipoCircuitoUrgencias getCircuitoUrgencias() {
        return circuitoUrgencias;
    }

    public void setCircuitoUrgencias(
            TipoCircuitoUrgencias circuitoUrgencias
    ) {
        this.circuitoUrgencias = circuitoUrgencias;
    }

    public ResultadoAtencionUrgencias getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoAtencionUrgencias resultado) {
        this.resultado = resultado;
    }

    public Hospital getHospitalAtencion() {
        return hospitalAtencion;
    }

    public void setHospitalAtencion(Hospital hospitalAtencion) {
        this.hospitalAtencion = hospitalAtencion;
    }

    public UbicacionUrgencias getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(UbicacionUrgencias ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public TipoCobertura getTipoCobertura() {
        return tipoCobertura;
    }

    public void setTipoCobertura(TipoCobertura tipoCobertura) {
        this.tipoCobertura = tipoCobertura;
    }

    public String getEntidadCobertura() {
        return entidadCobertura;
    }

    public void setEntidadCobertura(String entidadCobertura) {
        this.entidadCobertura = entidadCobertura;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public VinculoProfesionalAtencion getVinculoProfesional() {
        return vinculoProfesional;
    }

    public void setVinculoProfesional(
            VinculoProfesionalAtencion vinculoProfesional
    ) {
        this.vinculoProfesional = vinculoProfesional;
    }

    public boolean isAccidenteLaboral() {
        return accidenteLaboral;
    }

    public void setAccidenteLaboral(boolean accidenteLaboral) {
        this.accidenteLaboral = accidenteLaboral;
    }

    public boolean isBajoCustodiaPolicial() {
        return bajoCustodiaPolicial;
    }

    public void setBajoCustodiaPolicial(boolean bajoCustodiaPolicial) {
        this.bajoCustodiaPolicial = bajoCustodiaPolicial;
    }

    public List<ValoracionTriaje> getValoracionesTriaje() {
        return valoracionesTriaje;
    }

    public List<Interconsulta> getInterconsultas() {
        return interconsultas;
    }

    public List<SolicitudIngreso> getSolicitudesIngreso() {
        return solicitudesIngreso;
    }

    public List<TrasladoHospitalario> getTrasladosHospitalarios() {
        return trasladosHospitalarios;
    }

    /** Añade una valoración y establece la relación por ambos lados. */
    public void agregarValoracionTriaje(ValoracionTriaje valoracion) {
        if (valoracion != null && !valoracionesTriaje.contains(valoracion)) {
            valoracionesTriaje.add(valoracion);
            valoracion.setAtencionUrgencias(this);
        }
    }

    /** Añade una interconsulta y establece la relación por ambos lados. */
    public void agregarInterconsulta(Interconsulta interconsulta) {
        if (interconsulta != null
                && !interconsultas.contains(interconsulta)) {
            interconsultas.add(interconsulta);
            interconsulta.setAtencionUrgencias(this);
        }
    }

    /** Añade una solicitud de ingreso y establece la relación por ambos lados. */
    public void agregarSolicitudIngreso(
            SolicitudIngreso solicitudIngreso
    ) {
        if (solicitudIngreso != null
                && !solicitudesIngreso.contains(solicitudIngreso)) {
            solicitudesIngreso.add(solicitudIngreso);
            solicitudIngreso.setAtencionUrgencias(this);
        }
    }

    /** Añade un traslado y establece la relación por ambos lados. */
    public void agregarTrasladoHospitalario(
            TrasladoHospitalario traslado
    ) {
        if (traslado != null
                && !trasladosHospitalarios.contains(traslado)) {
            trasladosHospitalarios.add(traslado);
            traslado.setAtencionUrgencias(this);
        }
    }
}