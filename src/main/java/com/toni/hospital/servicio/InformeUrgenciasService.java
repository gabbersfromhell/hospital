package com.toni.hospital.servicio;

import com.toni.hospital.dto.InformeUrgenciasRequest;
import com.toni.hospital.modelo.AtencionUrgencias;
import com.toni.hospital.modelo.InformeUrgencias;
import com.toni.hospital.modelo.Medico;
import com.toni.hospital.repositorio.AtencionUrgenciasRepository;
import com.toni.hospital.repositorio.InformeUrgenciasRepository;
import com.toni.hospital.repositorio.MedicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InformeUrgenciasService {

    private final InformeUrgenciasRepository informeRepository;
    private final AtencionUrgenciasRepository atencionRepository;
    private final MedicoRepository medicoRepository;

    public InformeUrgenciasService(
            InformeUrgenciasRepository informeRepository,
            AtencionUrgenciasRepository atencionRepository,
            MedicoRepository medicoRepository
    ) {
        this.informeRepository = informeRepository;
        this.atencionRepository = atencionRepository;
        this.medicoRepository = medicoRepository;
    }

    @Transactional(readOnly = true)
    public List<InformeUrgencias> listarPorAtencion(Long atencionId) {
        comprobarAtencionExiste(atencionId);

        return informeRepository
                .findByAtencionUrgencias_IdOrderByFechaHoraEmisionDesc(
                        atencionId
                );
    }

    @Transactional(readOnly = true)
    public InformeUrgencias buscarPorAtencion(
            Long atencionId,
            Long informeId
    ) {
        comprobarAtencionExiste(atencionId);

        InformeUrgencias informe = buscarInformePorId(informeId);

        if (!informe.getAtencionUrgencias().getId().equals(atencionId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Informe no encontrado para esa atención"
            );
        }

        return informe;
    }

    @Transactional
    public InformeUrgencias crearInforme(
            Long atencionId,
            InformeUrgenciasRequest request
    ) {
        AtencionUrgencias atencion = comprobarAtencionExiste(atencionId);

        Medico medico = medicoRepository
                .findById(request.medicoResponsableId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médico responsable no encontrado"
                ));

        if (!medico.isActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El médico responsable está inactivo"
            );
        }

        if (!medico.getHospital().getId()
                .equals(atencion.getHospitalAtencion().getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El médico no pertenece al hospital que atendió al paciente"
            );
        }

        InformeUrgencias informe = new InformeUrgencias();
        informe.setAtencionUrgencias(atencion);
        informe.setMedicoResponsable(medico);
        informe.setFechaHoraEmision(LocalDateTime.now());
        informe.setResumenClinico(request.resumenClinico());
        informe.setDiagnostico(request.diagnostico());
        informe.setTratamiento(request.tratamiento());
        informe.setRecomendaciones(request.recomendaciones());

        return informeRepository.save(informe);
    }

    private InformeUrgencias buscarInformePorId(Long id) {
        return informeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Informe de Urgencias no encontrado"
                ));
    }

    private AtencionUrgencias comprobarAtencionExiste(Long atencionId) {
        return atencionRepository.findById(atencionId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Atención de Urgencias no encontrada"
                ));
    }
}