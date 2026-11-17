package com.toni.hospital.servicio;

import com.toni.hospital.modelo.NeveraMorgue;
import com.toni.hospital.repositorio.NeveraMorgueRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NeveraMorgueService {

    private final NeveraMorgueRepository neveraMorgueRepository;

    public NeveraMorgueService(NeveraMorgueRepository neveraMorgueRepository) {
        this.neveraMorgueRepository = neveraMorgueRepository;
    }

    public List<NeveraMorgue> listarNeveras() {
        return neveraMorgueRepository.findAll();
    }

    public NeveraMorgue buscarNeveraPorId(Long id) {
        return neveraMorgueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nevera de morgue no encontrada"
                ));
    }
}