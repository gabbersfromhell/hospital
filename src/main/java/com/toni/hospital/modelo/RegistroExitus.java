package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registra los datos administrativos del fallecimiento de un paciente
 * ocurrido durante una atención de Urgencias o un ingreso hospitalario.
 *
 * El servicio deberá validar que se indique exactamente uno de esos dos
 * contextos y que corresponda al mismo paciente.
 */
@Entity
@Table(name = "registros_exitus")
public class RegistroExitus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Paciente al que corresponde el registro.
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    // Atención de Urgencias en la que se produjo el fallecimiento, si aplica.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atencion_urgencias_id", unique = true)
    private AtencionUrgencias atencionUrgencias;

    // Ingreso durante el que se produjo el fallecimiento, si aplica.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingreso_hospitalario_id", unique = true)
    private IngresoHospitalario ingresoHospitalario;

    // Fecha y hora del fallecimiento.
    @Column(name = "fecha_hora_fallecimiento", nullable = false)
    private LocalDateTime fechaHoraFallecimiento;

    // Médico que cumplimenta el certificado médico de defunción.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_certificador_id")
    private Medico medicoCertificador;

    /*
     * Fecha de cumplimentación del certificado médico.
     * Si es null, todavía no consta como cumplimentado en el sistema.
     */
    @Column(name = "fecha_hora_certificado_medico")
    private LocalDateTime fechaHoraCertificadoMedico;

    // Nevera asignada mientras el paciente permanece en la morgue.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nevera_morgue_id")
    private NeveraMorgue neveraMorgue;

    // Fecha y hora de entrada en la morgue, si se traslada allí.
    @Column(name = "fecha_hora_entrada_morgue")
    private LocalDateTime fechaHoraEntradaMorgue;

    // Nombre de la funeraria, cuando se conoce.
    @Column(name = "nombre_funeraria", length = 150)
    private String nombreFuneraria;

    /*
     * Fecha y hora del aviso a la funeraria.
     * Si es null, el aviso todavía no consta como realizado.
     */
    @Column(name = "fecha_hora_aviso_funeraria")
    private LocalDateTime fechaHoraAvisoFuneraria;

    // Fecha y hora en que la funeraria retira al paciente del hospital.
    @Column(name = "fecha_hora_retirada")
    private LocalDateTime fechaHoraRetirada;

    // Constructor vacío requerido por JPA.
    public RegistroExitus() {
    }

    public Long getId() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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

    public LocalDateTime getFechaHoraFallecimiento() {
        return fechaHoraFallecimiento;
    }

    public void setFechaHoraFallecimiento(
            LocalDateTime fechaHoraFallecimiento
    ) {
        this.fechaHoraFallecimiento = fechaHoraFallecimiento;
    }

    public Medico getMedicoCertificador() {
        return medicoCertificador;
    }

    public void setMedicoCertificador(Medico medicoCertificador) {
        this.medicoCertificador = medicoCertificador;
    }

    public LocalDateTime getFechaHoraCertificadoMedico() {
        return fechaHoraCertificadoMedico;
    }

    public void setFechaHoraCertificadoMedico(
            LocalDateTime fechaHoraCertificadoMedico
    ) {
        this.fechaHoraCertificadoMedico = fechaHoraCertificadoMedico;
    }

    public NeveraMorgue getNeveraMorgue() {
        return neveraMorgue;
    }

    public void setNeveraMorgue(NeveraMorgue neveraMorgue) {
        this.neveraMorgue = neveraMorgue;
    }

    public LocalDateTime getFechaHoraEntradaMorgue() {
        return fechaHoraEntradaMorgue;
    }

    public void setFechaHoraEntradaMorgue(
            LocalDateTime fechaHoraEntradaMorgue
    ) {
        this.fechaHoraEntradaMorgue = fechaHoraEntradaMorgue;
    }

    public String getNombreFuneraria() {
        return nombreFuneraria;
    }

    public void setNombreFuneraria(String nombreFuneraria) {
        this.nombreFuneraria = nombreFuneraria;
    }

    public LocalDateTime getFechaHoraAvisoFuneraria() {
        return fechaHoraAvisoFuneraria;
    }

    public void setFechaHoraAvisoFuneraria(
            LocalDateTime fechaHoraAvisoFuneraria
    ) {
        this.fechaHoraAvisoFuneraria = fechaHoraAvisoFuneraria;
    }

    public LocalDateTime getFechaHoraRetirada() {
        return fechaHoraRetirada;
    }

    public void setFechaHoraRetirada(LocalDateTime fechaHoraRetirada) {
        this.fechaHoraRetirada = fechaHoraRetirada;
    }
}
