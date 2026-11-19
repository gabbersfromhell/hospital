package com.toni.hospital.modelo;

import jakarta.persistence.*;

/**
 * Producto del catálogo de medicamentos del hospital.
 *
 * Cada registro representa una presentación concreta. La pauta
 * y las dosis indicadas a un paciente se guardarán en la prescripción.
 */
@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre genérico o identificador habitual del producto. */
    @Column(name = "nombre_generico", nullable = false, length = 150)
    private String nombreGenerico;

    /** Nombre comercial, si se utiliza en el catálogo. */
    @Column(name = "nombre_comercial", length = 150)
    private String nombreComercial;

    /** Principio activo o combinación de principios activos. */
    @Column(name = "principios_activos", nullable = false, length = 250)
    private String principiosActivos;

    /** Concentración indicada en la presentación del producto. */
    @Column(nullable = false, length = 100)
    private String concentracion;

    /** Forma farmacéutica del producto. */
    @Enumerated(EnumType.STRING)
    @Column(name = "forma_farmaceutica", nullable = false, length = 40)
    private FormaFarmaceutica formaFarmaceutica;

    /** Descripción de la presentación, si se necesita. */
    @Column(length = 150)
    private String presentacion;

    /**
     * Permite retirar un producto del catálogo sin borrar sus
     * prescripciones históricas.
     */
    @Column(nullable = false)
    private boolean activo = true;

    /** Constructor vacío requerido por JPA. */
    public Medicamento() {
    }

    public Long getId() {
        return id;
    }

    public String getNombreGenerico() {
        return nombreGenerico;
    }

    public void setNombreGenerico(String nombreGenerico) {
        this.nombreGenerico = nombreGenerico;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getPrincipiosActivos() {
        return principiosActivos;
    }

    public void setPrincipiosActivos(String principiosActivos) {
        this.principiosActivos = principiosActivos;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public FormaFarmaceutica getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public void setFormaFarmaceutica(FormaFarmaceutica formaFarmaceutica) {
        this.formaFarmaceutica = formaFarmaceutica;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}