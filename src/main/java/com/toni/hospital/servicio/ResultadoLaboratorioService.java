package com.toni.hospital.servicio;

import com.toni.hospital.dto.ResultadoLaboratorioRequest;
import com.toni.hospital.modelo.EstadoResultadoLaboratorio;
import com.toni.hospital.modelo.ResultadoLaboratorio;
import com.toni.hospital.modelo.SolicitudPruebaDiagnostica;
import com.toni.hospital.modelo.TipoPruebaDiagnostica;
import com.toni.hospital.repositorio.ResultadoLaboratorioRepository;
import com.toni.hospital.repositorio.SolicitudPruebaDiagnosticaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/** Operaciones sobre resultados de laboratorio. */
@Service
public class ResultadoLaboratorioService {

    private final ResultadoLaboratorioRepository resultadoRepository;
    private final SolicitudPruebaDiagnosticaRepository solicitudRepository;

    public ResultadoLaboratorioService(
            ResultadoLaboratorioRepository resultadoRepository,
            SolicitudPruebaDiagnosticaRepository solicitudRepository
    ) {
        this.resultadoRepository = resultadoRepository;
        this.solicitudRepository = solicitudRepository;
    }

    /** Lista los resultados de una solicitud concreta. */
    public List<ResultadoLaboratorio> listarPorSolicitud(Long solicitudId) {
        if (!solicitudRepository.existsById(solicitudId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Solicitud de prueba diagnóstica no encontrada"
            );
        }

        return resultadoRepository
                .findBySolicitudPrueba_IdOrderByFechaHoraResultadoAsc(
                        solicitudId
                );
    }

    /** Busca un resultado por ID. */
    public ResultadoLaboratorio buscarPorId(Long id) {
        return resultadoRepository.findWithSolicitudById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Resultado de laboratorio no encontrado"
                ));
    }

    /** Registra un resultado pendiente de revisión. */
    @Transactional
    public ResultadoLaboratorio guardar(
            ResultadoLaboratorioRequest request
    ) {
        SolicitudPruebaDiagnostica solicitud = solicitudRepository
                .findWithDetallesById(request.solicitudPruebaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Solicitud de prueba diagnóstica no encontrada"
                ));

        if (solicitud.getTipo() != TipoPruebaDiagnostica.LABORATORIO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se pueden añadir resultados de laboratorio "
                            + "a una solicitud de tipo LABORATORIO"
            );
        }

        boolean tieneValorNumerico = request.valorNumerico() != null;
        boolean tieneValorTextual = request.valorTextual() != null
                && !request.valorTextual().isBlank();

        if (tieneValorNumerico == tieneValorTextual) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Indica un valor numérico o un valor textual, "
                            + "pero no ambos"
            );
        }

        ResultadoLaboratorio resultado = new ResultadoLaboratorio();
        resultado.setNombreDeterminacion(request.nombreDeterminacion());
        resultado.setValorNumerico(request.valorNumerico());
        resultado.setValorTextual(request.valorTextual());
        resultado.setUnidadMedida(request.unidadMedida());
        resultado.setIntervaloReferencia(request.intervaloReferencia());
        resultado.setFechaHoraResultado(LocalDateTime.now());
        resultado.setEstado(
                EstadoResultadoLaboratorio.PENDIENTE_REVISION
        );

        solicitud.agregarResultadoLaboratorio(resultado);

        return resultadoRepository.save(resultado);
    }
}