package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.AtencionUCSI;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AtencionUCSIRepository
        extends JpaRepository<AtencionUCSI, Long> {

    @EntityGraph(attributePaths = {
            "intervencionQuirurgica",
            "intervencionQuirurgica.paciente",
            "ingresoHospitalarioPosterior"
    })
    List<AtencionUCSI> findAllByOrderByFechaHoraEntradaAsc();

    @EntityGraph(attributePaths = {
            "intervencionQuirurgica",
            "intervencionQuirurgica.paciente",
            "ingresoHospitalarioPosterior"
    })
    Optional<AtencionUCSI> findWithDetallesById(Long id);

    boolean existsByIntervencionQuirurgica_Id(Long intervencionId);
}
