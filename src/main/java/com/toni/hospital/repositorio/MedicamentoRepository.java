package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/** Acceso al catálogo de medicamentos. */
public interface MedicamentoRepository
        extends JpaRepository<Medicamento, Long> {

    /** Lista los medicamentos activos en orden alfabético. */
    List<Medicamento> findByActivoTrueOrderByNombreGenericoAsc();

    /** Busca medicamentos activos cuyo nombre genérico contenga el texto indicado. */
    List<Medicamento>
    findByNombreGenericoContainingIgnoreCaseAndActivoTrueOrderByNombreGenericoAsc(
            String nombre
    );

    /** Busca medicamentos activos por principio activo. */
    List<Medicamento>
    findByPrincipiosActivosContainingIgnoreCaseAndActivoTrueOrderByNombreGenericoAsc(
            String principioActivo
    );
}