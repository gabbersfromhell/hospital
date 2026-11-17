package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Planta de un hospital.
 */
@Entity
@Table(
        name = "plantas",
        // No puede repetirse el número de planta dentro del mismo hospital.
        uniqueConstraints = @UniqueConstraint(
                name = "uk_planta_hospital_numero",
                columnNames = {"hospital_id", "numero_planta"}
        )
)
public class Planta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número con el que el hospital identifica esta planta.
    @Column(name = "numero_planta", nullable = false)
    private Integer numeroPlanta;

    /*
     * Varias plantas pertenecen a un mismo hospital.
     * La clave foránea hospital_id se guarda en la tabla plantas.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public Planta() {
    }

    public Long getId() {
        return id;
    }

    public Integer getNumeroPlanta() {
        return numeroPlanta;
    }

    public void setNumeroPlanta(Integer numeroPlanta) {
        this.numeroPlanta = numeroPlanta;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}