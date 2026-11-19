package com.toni.hospital.servicio;

import com.toni.hospital.dto.NeveraMorgueRequest;
import com.toni.hospital.modelo.Hospital;
import com.toni.hospital.modelo.NeveraMorgue;
import com.toni.hospital.repositorio.HospitalRepository;
import com.toni.hospital.repositorio.NeveraMorgueRepository;
import com.toni.hospital.repositorio.RegistroExitusRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class NeveraMorgueService {

    private final NeveraMorgueRepository neveraMorgueRepository;
    private final HospitalRepository hospitalRepository;
    private final RegistroExitusRepository registroExitusRepository;

    public NeveraMorgueService(
            NeveraMorgueRepository neveraMorgueRepository,
            HospitalRepository hospitalRepository,
            RegistroExitusRepository registroExitusRepository
    ) {
        this.neveraMorgueRepository = neveraMorgueRepository;
        this.hospitalRepository = hospitalRepository;
        this.registroExitusRepository = registroExitusRepository;
    }

    /** Devuelve todas las neveras registradas, activas o inactivas. */
    public List<NeveraMorgue> listarNeveras() {
        return neveraMorgueRepository.findAll();
    }

    /** Devuelve solo las neveras activas. */
    public List<NeveraMorgue> listarNeverasActivas() {
        return neveraMorgueRepository.findByActivaTrue();
    }

    /** Busca una nevera por su identificador. */
    public NeveraMorgue buscarNeveraPorId(Long id) {
        return neveraMorgueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Nevera de morgue no encontrada"
                ));
    }

    /** Indica si hay un exitus que todavía no ha sido retirado de esa nevera. */
    public boolean estaOcupada(Long neveraId) {
        return registroExitusRepository
                .existsByNeveraMorgue_IdAndFechaHoraRetiradaIsNull(neveraId);
    }

    /** Crea una nevera asociada a un hospital existente. */
    @Transactional
    public NeveraMorgue crearNevera(NeveraMorgueRequest request) {
        validarCodigoDuplicado(
                request.hospitalId(),
                request.codigo(),
                null
        );

        Hospital hospital = buscarHospital(request.hospitalId());

        NeveraMorgue nevera = new NeveraMorgue();
        nevera.setCodigo(request.codigo());
        nevera.setUbicacion(request.ubicacion());
        nevera.setHospital(hospital);
        nevera.setActiva(request.activa());

        return neveraMorgueRepository.save(nevera);
    }

    /** Actualiza los datos de una nevera existente. */
    @Transactional
    public NeveraMorgue actualizarNevera(
            Long id,
            NeveraMorgueRequest request
    ) {
        NeveraMorgue nevera = buscarNeveraPorId(id);

        validarCodigoDuplicado(
                request.hospitalId(),
                request.codigo(),
                id
        );

        // No se puede desactivar una nevera mientras tenga un ocupante.
        if (!request.activa() && estaOcupada(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede desactivar una nevera que está ocupada"
            );
        }

        Hospital hospital = buscarHospital(request.hospitalId());

        nevera.setCodigo(request.codigo());
        nevera.setUbicacion(request.ubicacion());
        nevera.setHospital(hospital);
        nevera.setActiva(request.activa());

        return neveraMorgueRepository.save(nevera);
    }

    /**
     * Desactiva la nevera conservando el registro histórico.
     * No la elimina físicamente de la base de datos.
     */
    @Transactional
    public void desactivarNevera(Long id) {
        NeveraMorgue nevera = buscarNeveraPorId(id);

        if (estaOcupada(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede desactivar una nevera que está ocupada"
            );
        }

        nevera.setActiva(false);
        neveraMorgueRepository.save(nevera);
    }

    private Hospital buscarHospital(Long hospitalId) {
        return hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Hospital no encontrado"
                ));
    }

    private void validarCodigoDuplicado(
            Long hospitalId,
            String codigo,
            Long idActual
    ) {
        boolean duplicado;

        if (idActual == null) {
            duplicado = neveraMorgueRepository
                    .existsByHospital_IdAndCodigo(hospitalId, codigo);
        } else {
            duplicado = neveraMorgueRepository
                    .existsByHospital_IdAndCodigoAndIdNot(
                            hospitalId,
                            codigo,
                            idActual
                    );
        }

        if (duplicado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una nevera con ese código en el hospital"
            );
        }
    }
}