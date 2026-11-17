package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Persona a la que se puede llamar por un paciente.
 */
@Entity
@Table(name = "contactos_paciente")
public class ContactoPaciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    // Ejemplos: familiar, pareja, tutor o el propio paciente.
    @Column(name = "relacion_con_paciente", nullable = false)
    private String relacionConPaciente;

    @Column(nullable = false)
    private String telefono;

    // Indica si este es el contacto prioritario para llamar.
    @Column(name = "contacto_principal", nullable = false)
    private boolean contactoPrincipal;

    // Este lado posee la relación y contiene la clave foránea paciente_id.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Constructor vacío necesario para JPA.
    public ContactoPaciente() {
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

    public String getRelacionConPaciente() {
        return relacionConPaciente;
    }

    public void setRelacionConPaciente(String relacionConPaciente) {
        this.relacionConPaciente = relacionConPaciente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(boolean contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}