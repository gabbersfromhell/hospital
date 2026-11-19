package com.toni.hospital.servicio;

import com.toni.hospital.dto.AtencionUCSIRequest;
import com.toni.hospital.dto.CambiarEstadoAtencionUCSIRequest;
import com.toni.hospital.modelo.*;
import com.toni.hospital.repositorio.AtencionUCSIRepository;
import com.toni.hospital.repositorio.IngresoHospitalarioRepository;
import com.toni.hospital.repositorio.IntervencionQuirurgicaRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AtencionUCSIService {

    private final AtencionUCSIRepository atencionRepository;
    private final IntervencionQuirurgicaRepository intervencionRepository;
    private final IngresoHospitalarioRepository ingresoRepository;

    public AtencionUCSIService(
            AtencionUCSIRepository atencionRepository,
            IntervencionQuirurgicaRepository intervencionRepository,
            IngresoHospitalarioRepository ingresoRepository
    ) {
        this.atencionRepository = atencionRepository;
        this.intervencionRepository = intervencionRepository;
        this.ingresoRepository = ingresoRepository;
    }

    public List<AtencionUCSI> listar() {
        return atencionRepository.findAllByOrderByFechaHoraEntradaAsc();
    }

    public AtencionUCSI buscarPorId(Long id) {
        return atencionRepository.findWithDetallesById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Atención UCSI no encontrada"
                ));
    }

    @Transactional
    public AtencionUCSI crear(AtencionUCSIRequest request) {
        IntervencionQuirurgica intervencion =
                intervencionRepository.findWithDetallesById(
                                request.intervencionQuirurgicaId()
                        )
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Intervención quirúrgica no encontrada"
                        ));

        if (intervencion.getCitaAmbulatoria() == null
                || intervencion.getIngresoHospitalario() != null
                || intervencion.getAtencionUrgencias() != null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La atención UCSI debe corresponder a una intervención "
                            + "ambulatoria"
            );
        }

        if (intervencion.getEstado()
                != EstadoIntervencionQuirurgica.PROGRAMADA
                && intervencion.getEstado()
                != EstadoIntervencionQuirurgica.CONFIRMADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se puede crear una atención UCSI para una "
                            + "intervención programada o confirmada"
            );
        }

        if (atencionRepository.existsByIntervencionQuirurgica_Id(
                intervencion.getId()
        )) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "La intervención ya tiene una atención UCSI"
            );
        }

        AtencionUCSI atencion = new AtencionUCSI();
        atencion.setIntervencionQuirurgica(intervencion);
        atencion.setEstado(EstadoAtencionUCSI.PROGRAMADA);
        atencion.setObservaciones(request.observaciones());
        return atencionRepository.save(atencion);
    }

    @Transactional
    public AtencionUCSI cambiarEstado(
            Long id, CambiarEstadoAtencionUCSIRequest request
    ) {
        AtencionUCSI atencion = buscarPorId(id);
        EstadoAtencionUCSI actual = atencion.getEstado();
        EstadoAtencionUCSI nuevo = request.nuevoEstado();
        LocalDateTime ahora = LocalDateTime.now();

        if (nuevo == EstadoAtencionUCSI.EN_PREPARACION
                && actual == EstadoAtencionUCSI.PROGRAMADA) {
            atencion.setFechaHoraEntrada(ahora);
        } else if (nuevo == EstadoAtencionUCSI.EN_INTERVENCION
                && actual == EstadoAtencionUCSI.EN_PREPARACION) {
            atencion.setFechaHoraEntradaQuirofano(ahora);
            IntervencionQuirurgica intervencion =
                    atencion.getIntervencionQuirurgica();
            intervencion.setFechaHoraInicioReal(ahora);
            intervencion.setEstado(
                    EstadoIntervencionQuirurgica.EN_CURSO
            );
            intervencionRepository.save(intervencion);
        } else if (nuevo == EstadoAtencionUCSI.EN_RECUPERACION
                && actual == EstadoAtencionUCSI.EN_INTERVENCION) {
            atencion.setFechaHoraSalidaQuirofano(ahora);
            atencion.setFechaHoraInicioRecuperacion(ahora);
            IntervencionQuirurgica intervencion =
                    atencion.getIntervencionQuirurgica();
            intervencion.setFechaHoraFinReal(ahora);
            intervencion.setEstado(
                    EstadoIntervencionQuirurgica.FINALIZADA
            );
            intervencionRepository.save(intervencion);
        } else if (nuevo == EstadoAtencionUCSI.ALTA_DOMICILIARIA
                && actual == EstadoAtencionUCSI.EN_RECUPERACION) {
            atencion.setFechaHoraAlta(ahora);
        } else if (nuevo == EstadoAtencionUCSI.INGRESO_HOSPITALARIO
                && actual == EstadoAtencionUCSI.EN_RECUPERACION) {
            if (request.ingresoHospitalarioId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Indica el ingreso hospitalario asociado"
                );
            }
            IngresoHospitalario ingreso = ingresoRepository
                    .findById(request.ingresoHospitalarioId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Ingreso hospitalario no encontrado"
                    ));
            Long pacienteIngresoId = ingreso.getSolicitudIngreso()
                    .getAtencionUrgencias().getPaciente().getId();
            Long pacienteIntervencionId = atencion
                    .getIntervencionQuirurgica().getPaciente().getId();
            if (!pacienteIngresoId.equals(pacienteIntervencionId)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El ingreso posterior corresponde a otro paciente"
                );
            }
            atencion.setIngresoHospitalarioPosterior(ingreso);
            atencion.setFechaHoraAlta(ahora);
        } else if (nuevo == EstadoAtencionUCSI.CANCELADA
                && (actual == EstadoAtencionUCSI.PROGRAMADA
                || actual == EstadoAtencionUCSI.EN_PREPARACION)) {
            atencion.getIntervencionQuirurgica().setEstado(
                    EstadoIntervencionQuirurgica.CANCELADA
            );
            intervencionRepository.save(
                    atencion.getIntervencionQuirurgica()
            );
        } else {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Transición de estado UCSI no permitida: "
                            + actual + " -> " + nuevo
            );
        }

        atencion.setEstado(nuevo);
        return atencionRepository.save(atencion);
    }
}
