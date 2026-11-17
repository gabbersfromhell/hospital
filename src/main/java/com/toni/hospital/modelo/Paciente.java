package com.toni.hospital.modelo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Datos personales y de contacto de un paciente.
 */
@Entity
@Table(
        name = "pacientes",
        // Evita repetir la misma combinación de tipo y número de documento.
        uniqueConstraints = @UniqueConstraint(
                name = "uk_paciente_documento",
                columnNames = {"tipo_documento", "numero_documento"}
        )
)
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellidos;

    /*
     * Puede quedar vacío si el paciente llega a urgencias
     * sin que se conozca todavía su documento.
     *
     * EnumType.STRING guarda el nombre del valor, por ejemplo "DNI".
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(name = "numero_documento", length = 30)
    private String numeroDocumento;

    /*
     * Código identificativo personal de CatSalut.
     * Puede no estar disponible en el primer momento de la atención.
     */
    @Column(length = 30, unique = true)
    private String cip;

    /*
     * Dato administrativo opcional.
     * No determina por sí solo quién financia una atención;
     * esa cobertura se guardará en AtencionUrgencias.
     */
    @Column(name = "numero_seguridad_social", length = 30, unique = true)
    private String numeroSeguridadSocial;

    @Column(length = 250)
    private String direccion;

    @Column(name = "correo_electronico", length = 150)
    private String correoElectronico;

    /*
     * Población donde reside el paciente.
     * Desde Poblacion se puede consultar su hospital territorial
     * de referencia. No indica dónde se atiende una visita concreta.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poblacion_id")
    private Poblacion poblacion;

    /*
     * Un paciente puede tener varios contactos.
     * La clave foránea paciente_id está en ContactoPaciente,
     * que es el lado propietario de la relación.
     */
    @OneToMany(
            mappedBy = "paciente",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ContactoPaciente> contactos = new ArrayList<>();

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public Paciente() {
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

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getCip() {
        return cip;
    }

    public void setCip(String cip) {
        this.cip = cip;
    }

    public String getNumeroSeguridadSocial() {
        return numeroSeguridadSocial;
    }

    public void setNumeroSeguridadSocial(String numeroSeguridadSocial) {
        this.numeroSeguridadSocial = numeroSeguridadSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Poblacion getPoblacion() {
        return poblacion;
    }

    public void setPoblacion(Poblacion poblacion) {
        this.poblacion = poblacion;
    }

    public List<ContactoPaciente> getContactos() {
        return contactos;
    }

    /**
     * Añade un contacto y establece también la relación en ContactoPaciente.
     */
    public void agregarContacto(ContactoPaciente contacto) {
        if (contacto != null && !contactos.contains(contacto)) {
            contactos.add(contacto);
            contacto.setPaciente(this);
        }
    }

    /**
     * Quita un contacto de la lista.
     * Como usamos orphanRemoval = true, JPA eliminará ese contacto
     * de la base de datos al guardar los cambios.
     */
    public void quitarContacto(ContactoPaciente contacto) {
        if (contactos.remove(contacto)) {
            contacto.setPaciente(null);
        }
    }
}