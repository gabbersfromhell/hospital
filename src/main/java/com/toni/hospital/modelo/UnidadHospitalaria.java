package com.toni.hospital.modelo;

import jakarta.persistence.*;

@Entity
@Table(
        name = "unidades_hospitalarias",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_unidad_hospital_codigo",
                columnNames = {"hospital_id", "codigo"}
        )
)
public class UnidadHospitalaria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código interno de la unidad, único dentro de su hospital.
    @Column(nullable = false, length = 30)
    private String codigo;

    // Nombre legible, por ejemplo: "Unidad de Cuidados Paliativos".
    @Column(nullable = false, length = 120)
    private String nombre;

    // Hospital al que pertenece esta unidad.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    /*
     * Servicio clínico relacionado con la unidad.
     * Puede ser null si todavía no lo hemos asignado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_hospitalario_id")
    private ServicioHospitalario servicioHospitalario;

    // Permite dejar de usar la unidad sin borrar su registro.
    @Column(nullable = false)
    private boolean activa = true;

    // Constructor vacío requerido por JPA.
    public UnidadHospitalaria() {
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public void setServicioHospitalario(ServicioHospitalario servicioHospitalario) {
        this.servicioHospitalario = servicioHospitalario;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}