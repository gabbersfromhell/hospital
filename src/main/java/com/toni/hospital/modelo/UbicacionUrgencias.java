package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Espacio de urgencias en el que puede encontrarse el paciente.
 *
 * Ejemplos de código: BOX-01, CAM-03, OBS-01.
 * Se crea un registro por espacio real, sin fijar el número de espacios.
 */
@Entity
@Table(
        name = "ubicaciones_urgencias",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_ubicacion_hospital_codigo",
                columnNames = {"hospital_id", "codigo"}
        )
)
public class UbicacionUrgencias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código que permite identificar el espacio concreto.
    @Column(nullable = false, length = 30)
    private String codigo;

    // Hospital al que pertenece este espacio de Urgencias.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    /*
     * Clasifica el espacio: BOX, CAMILLA, OBSERVACION, UCI, etc.
     * Se almacena el nombre del enum como texto.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUbicacionUrgencias tipo;

    /*
     * Permite dejar de asignar pacientes a este espacio sin borrar
     * el registro ni su historial asociado.
     */
    @Column(nullable = false)
    private boolean activa = true;

    /**
     * Constructor vacío necesario para que JPA pueda crear la entidad.
     */
    public UbicacionUrgencias() {
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

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public TipoUbicacionUrgencias getTipo() {
        return tipo;
    }

    public void setTipo(TipoUbicacionUrgencias tipo) {
        this.tipo = tipo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
