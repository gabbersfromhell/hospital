package com.toni.hospital.modelo;

import jakarta.persistence.*;

@Entity
@Table(
        name = "neveras_morgue",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_nevera_hospital_codigo",
                columnNames = {"hospital_id", "codigo"}
        )
)
public class NeveraMorgue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Código o número que permite identificar la nevera.
    @Column(nullable = false, length = 30)
    private String codigo;

    // Ubicación descriptiva, por ejemplo: "Morgue, planta -1".
    @Column(length = 120)
    private String ubicacion;

    // Hospital al que pertenece la nevera.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    // Permite dejar de usarla sin borrar su registro.
    @Column(nullable = false)
    private boolean activa = true;

    // Constructor vacío requerido por JPA.
    public NeveraMorgue() {
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}