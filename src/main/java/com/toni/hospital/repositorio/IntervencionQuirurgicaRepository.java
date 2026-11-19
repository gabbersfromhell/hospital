package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.IntervencionQuirurgica;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IntervencionQuirurgicaRepository
        extends JpaRepository<IntervencionQuirurgica, Long> {

    @EntityGraph(attributePaths = {
            "paciente", "hospital", "servicioQuirurgico", "salaQuirurgica",
            "medicoResponsable", "atencionUrgencias",
            "ingresoHospitalario", "citaAmbulatoria"
    })
    List<IntervencionQuirurgica>
    findAllByOrderByFechaHoraProgramadaAsc();

    @EntityGraph(attributePaths = {
            "paciente", "hospital", "servicioQuirurgico", "salaQuirurgica",
            "medicoResponsable", "atencionUrgencias",
            "ingresoHospitalario", "citaAmbulatoria"
    })
    Optional<IntervencionQuirurgica> findWithDetallesById(Long id);

    @EntityGraph(attributePaths = {"salaQuirurgica", "paciente"})
    List<IntervencionQuirurgica>
    findBySalaQuirurgica_IdOrderByFechaHoraProgramadaAsc(Long salaId);
}
