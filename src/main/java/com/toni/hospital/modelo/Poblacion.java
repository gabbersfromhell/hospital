package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Población donde residen pacientes.
 */
@Entity
@Table(name = "poblaciones")
public class Poblacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    /*
     * Hospital territorial de referencia de esta población.
     *
     * Varias poblaciones pueden tener el mismo hospital de referencia.
     * La clave foránea hospital_referencia_id se guarda en la tabla poblaciones.
     *
     * El hospital que atiende una urgencia concreta se guarda en
     * AtencionUrgencias; puede ser distinto de este hospital.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_referencia_id")
    private Hospital hospitalReferencia;

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public Poblacion() {
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

    public Hospital getHospitalReferencia() {
        return hospitalReferencia;
    }

    public void setHospitalReferencia(Hospital hospitalReferencia) {
        this.hospitalReferencia = hospitalReferencia;
    }
}