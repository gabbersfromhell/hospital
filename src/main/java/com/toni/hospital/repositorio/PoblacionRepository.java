package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Poblacion;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PoblacionRepository extends JpaRepository<Poblacion, Long> {

    @Override
    @EntityGraph(attributePaths = "hospitalReferencia")
    List<Poblacion> findAll();

    @Override
    @EntityGraph(attributePaths = "hospitalReferencia")
    Optional<Poblacion> findById(Long id);
}