package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Servicio que puede ofrecer un hospital.
 *
 * Ejemplos: Cardiología, Traumatología, Laboratorio o Farmacia.
 */
@Entity
@Table(name = "servicios_hospitalarios")
public class ServicioHospitalario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre que verá la aplicación, por ejemplo "Cardiología".
    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    // Grupo al que pertenece este servicio.
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", nullable = false)
    private TipoServicioHospitalario tipoServicio;

    /*
     * Permite dejar de ofrecer el servicio en el sistema sin borrarlo,
     * conservando las referencias históricas.
     */
    @Column(nullable = false)
    private boolean activo = true;

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public ServicioHospitalario() {
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

    public TipoServicioHospitalario getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicioHospitalario tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}