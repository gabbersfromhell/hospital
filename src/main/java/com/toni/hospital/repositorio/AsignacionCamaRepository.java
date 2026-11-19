package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.AsignacionCama;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsignacionCamaRepository
        extends JpaRepository<AsignacionCama, Long> {

    /**
     * Devuelve el historial de asignaciones de un ingreso,
     * ordenado desde la más antigua.
     */
    @EntityGraph(attributePaths = {
            "ingresoHospitalario",
            "cama.habitacion.planta"
    })
    List<AsignacionCama>
    findByIngresoHospitalario_IdOrderByFechaHoraInicioAsc(Long ingresoId);

    /** Devuelve las asignaciones que siguen activas para un ingreso. */
    List<AsignacionCama>
    findByIngresoHospitalario_IdAndFechaHoraFinIsNull(Long ingresoId);

    /**
     * Busca una asignación por ID y carga los datos necesarios
     * para construir su respuesta.
     */
    @Override
    @EntityGraph(attributePaths = {
            "ingresoHospitalario",
            "cama.habitacion.planta"
    })
    Optional<AsignacionCama> findById(Long id);

    /** Comprueba si la cama tiene actualmente una asignación activa. */
    boolean existsByCama_IdAndFechaHoraFinIsNull(Long camaId);

    /** Comprueba si la cama aparece en alguna asignación, incluso histórica. */
    boolean existsByCama_Id(Long camaId);
}