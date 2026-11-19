package com.toni.hospital.servicio;

import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.JustificanteAsistencia;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.JustificanteAsistenciaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JustificanteAsistenciaService {

    private final JustificanteAsistenciaRepository justificanteRepository;
    private final AtencionUrgenciasRepository atencionRepository;

    public JustificanteAsistenciaService(
            JustificanteAsistenciaRepository justificanteRepository,
            AtencionUrgenciasRepository atencionRepository
    ) {
        this.justificanteRepository = justificanteRepository;
        this.atencionRepository = atencionRepository;
    }

    @Transactional(readOnly = true)
    public List<JustificanteAsistencia> listarPorAtencion(Long atencionId) {
        comprobarAtencionExiste(atencionId);

        return justificanteRepository
                .findByAtencionUrgencias_IdOrderByFechaHoraEmisionDesc(
                        atencionId
                );
    }

    @Transactional(readOnly = true)
    public JustificanteAsistencia buscarPorAtencion(
            Long atencionId,
            Long justificanteId
    ) {
        comprobarAtencionExiste(atencionId);

        JustificanteAsistencia justificante =
                justificanteRepository.findById(justificanteId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Justificante no encontrado"
                        ));

        if (!justificante.getAtencionUrgencias()
                .getId()
                .equals(atencionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Justificante no encontrado para esa atención"
            );
        }

        return justificante;
    }

    /**
     * Emite el justificante cuando la atención ya tiene hora de salida.
     */
    @Transactional
    public JustificanteAsistencia emitir(Long atencionId) {
        AtencionUrgencias atencion = comprobarAtencionExiste(atencionId);

        if (atencion.getFechaHoraFinUrgencias() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La atención debe finalizarse antes de emitir el justificante"
            );
        }

        JustificanteAsistencia justificante =
                new JustificanteAsistencia();

        justificante.setAtencionUrgencias(atencion);
        justificante.setFechaHoraEmision(LocalDateTime.now());

        return justificanteRepository.save(justificante);
    }

    private AtencionUrgencias comprobarAtencionExiste(Long atencionId) {
        return atencionRepository.findById(atencionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de Urgencias no encontrada"
                ));
    }
}