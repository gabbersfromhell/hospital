package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Representa una cama física de una habitación.
 *
 * La ocupación se consulta mediante las asignaciones de cama:
 * una asignación sin fecha de fin significa que la cama está ocupada.
 */
@Entity
@Table(
        name = "camas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_cama_habitacion_numero",
                columnNames = {"habitacion_id", "numero_cama"}
        )
)
public class Cama {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Número de cama dentro de la habitación, por ejemplo 1 o 2.
    @Column(name = "numero_cama", nullable = false)
    private Integer numeroCama;

    // Habitación a la que pertenece esta cama.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "habitacion_id", nullable = false)
    private Habitacion habitacion;

    /*
     * Estado operativo de la cama.
     * La ocupación se determina con AsignacionCama, no con este enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCama estado = EstadoCama.OPERATIVA;

    // Constructor vacío requerido por JPA.
    public Cama() {
    }

    public Long getId() {
        return id;
    }

    public Integer getNumeroCama() {
        return numeroCama;
    }

    public void setNumeroCama(Integer numeroCama) {
        this.numeroCama = numeroCama;
    }

    public Habitacion getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(Habitacion habitacion) {
        this.habitacion = habitacion;
    }

    public EstadoCama getEstado() {
        return estado;
    }

    public void setEstado(EstadoCama estado) {
        this.estado = estado;
    }
}