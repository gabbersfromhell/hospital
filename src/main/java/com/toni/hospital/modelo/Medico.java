package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Profesional médico que trabaja en un hospital.
 *
 * Esta entidad es una versión sencilla para el proyecto de aprendizaje;
 * no pretende representar el registro completo de personal del hospital.
 */
@Entity
@Table(
        name = "medicos",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_medico_numero_colegiado",
                columnNames = "numero_colegiado"
        )
)
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    // Identificador profesional del médico en este modelo.
    @Column(name = "numero_colegiado", nullable = false, length = 40)
    private String numeroColegiado;

    // Hospital en el que trabaja el médico.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    // Servicio al que está adscrito, si se ha registrado.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_hospitalario_id")
    private ServicioHospitalario servicioHospitalario;

    // Permite desactivar al médico sin borrar referencias históricas.
    @Column(nullable = false)
    private boolean activo = true;

    // Constructor vacío requerido por JPA.
    public Medico() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public ServicioHospitalario getServicioHospitalario() {
        return servicioHospitalario;
    }

    public void setServicioHospitalario(
            ServicioHospitalario servicioHospitalario
    ) {
        this.servicioHospitalario = servicioHospitalario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
