package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}