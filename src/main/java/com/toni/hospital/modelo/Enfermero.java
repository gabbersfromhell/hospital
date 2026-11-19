package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Profesional de Enfermería que trabaja en un hospital.
 *
 * En este modelo sencillo permite identificar quién realizó
 * una valoración de triaje.
 */
@Entity
@Table(
        name = "enfermeros",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enfermero_numero_colegiado",
                columnNames = "numero_colegiado"
        )
)
public class Enfermero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    // Identificador profesional en este modelo de aprendizaje.
    @Column(name = "numero_colegiado", nullable = false, length = 40)
    private String numeroColegiado;

    // Hospital en el que trabaja el enfermero o enfermera.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    // Permite desactivar al profesional sin borrar el historial de triajes.
    @Column(nullable = false)
    private boolean activo = true;

    /** Constructor vacío necesario para que JPA cree la entidad. */
    public Enfermero() {
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}