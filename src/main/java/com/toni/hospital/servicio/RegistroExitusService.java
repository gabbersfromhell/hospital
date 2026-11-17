package com.toni.hospital.servicio;

import com.toni.hospital.modelo.RegistroExitus;
import com.toni.hospital.repositorio.RegistroExitusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class RegistroExitusService {

    private final RegistroExitusRepository registroExitusRepository;

    public RegistroExitusService(
            RegistroExitusRepository registroExitusRepository
    ) {
        this.registroExitusRepository = registroExitusRepository;
    }

    public List<RegistroExitus> listarRegistrosExitus() {
        return registroExitusRepository.findAll();
    }

    public RegistroExitus buscarRegistroExitusPorId(Long id) {
        return registroExitusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Registro de exitus no encontrado"
                ));
    }
}