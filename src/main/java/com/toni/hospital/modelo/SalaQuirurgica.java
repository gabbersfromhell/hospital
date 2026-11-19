package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Sala del bloque quirúrgico. El tipo distingue quirófanos y quirófanitos.
 */
@Entity
@Table(
        name = "salas_quirurgicas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_sala_quirurgica_hospital_codigo",
                columnNames = {"hospital_id", "codigo"}
        )
)
public class SalaQuirurgica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false, length = 30)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoSalaQuirurgica tipo;

    @Column(length = 150)
    private String ubicacion;

    @Column(nullable = false)
    private boolean activa = true;

    public SalaQuirurgica() {
    }

    public Long getId() {
        return id;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
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

    public TipoSalaQuirurgica getTipo() {
        return tipo;
    }

    public void setTipo(TipoSalaQuirurgica tipo) {
        this.tipo = tipo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
