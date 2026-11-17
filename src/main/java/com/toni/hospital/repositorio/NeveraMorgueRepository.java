package com.toni.hospital.repositorio;

import com.toni.hospital.modelo.NeveraMorgue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NeveraMorgueRepository
        extends JpaRepository<NeveraMorgue, Long> {
}