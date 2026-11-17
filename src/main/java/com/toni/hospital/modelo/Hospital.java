package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Centro hospitalario donde puede atenderse o ingresarse un paciente.
 */
@Entity
@Table(name = "hospitales")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String municipio;

    @Column(length = 250)
    private String direccion;

    @Column(length = 30)
    private String telefono;

    /*
     * Un hospital puede ofrecer varios servicios y un mismo servicio,
     * como Cardiología, puede estar disponible en varios hospitales.
     *
     * JPA crea la tabla intermedia hospital_servicios para esta relación.
     * Este es el lado que gestiona la tabla intermedia.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "hospital_servicios",
            joinColumns = @JoinColumn(name = "hospital_id"),
            inverseJoinColumns = @JoinColumn(name = "servicio_hospitalario_id")
    )
    private List<ServicioHospitalario> servicios = new ArrayList<>();

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public Hospital() {
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

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<ServicioHospitalario> getServicios() {
        return servicios;
    }

    /**
     * Añade un servicio a la lista de servicios ofrecidos por el hospital.
     */
    public void agregarServicio(ServicioHospitalario servicio) {
        if (servicio != null && !servicios.contains(servicio)) {
            servicios.add(servicio);
        }
    }

    /**
     * Quita un servicio de la lista de servicios del hospital.
     * No elimina la entidad ServicioHospitalario de la base de datos.
     */
    public void quitarServicio(ServicioHospitalario servicio) {
        servicios.remove(servicio);
    }
}