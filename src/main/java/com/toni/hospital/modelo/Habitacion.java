package com.toni.hospital.modelo;

import jakarta.persistence.*;

@Entity
@Table(
        name = "habitaciones",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_habitacion_planta_numero",
                columnNames = {"planta_id", "numero_habitacion"}
        )
)
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número o código de la habitación, por ejemplo "101".
    @Column(name = "numero_habitacion", nullable = false, length = 20)
    private String numeroHabitacion;

    // Planta física donde está la habitación.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planta_id", nullable = false)
    private Planta planta;

    // Unidad hospitalaria a la que está asignada la habitación.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "unidad_hospitalaria_id", nullable = false)
    private UnidadHospitalaria unidadHospitalaria;

    // Constructor vacío requerido por JPA.
    public Habitacion() {
    }

    public Long getId() {
        return id;
    }

    public String getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(String numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public UnidadHospitalaria getUnidadHospitalaria() {
        return unidadHospitalaria;
    }

    public void setUnidadHospitalaria(UnidadHospitalaria unidadHospitalaria) {
        this.unidadHospitalaria = unidadHospitalaria;
    }
}