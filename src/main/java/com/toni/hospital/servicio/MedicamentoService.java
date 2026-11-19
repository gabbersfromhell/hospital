package com.toni.hospital.servicio;

import com.toni.hospital.dto.MedicamentoRequest;
import com.toni.hospital.modelo.Medicamento;
import com.toni.hospital.repositorio.MedicamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/** Operaciones del catálogo de medicamentos. */
@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    /** Lista los medicamentos activos. */
    public List<Medicamento> listarActivos() {
        return medicamentoRepository.findByActivoTrueOrderByNombreGenericoAsc();
    }

    /** Busca medicamentos activos por nombre genérico. */
    public List<Medicamento> buscarPorNombre(String nombre) {
        return medicamentoRepository
                .findByNombreGenericoContainingIgnoreCaseAndActivoTrueOrderByNombreGenericoAsc(
                        nombre
                );
    }

    /** Busca medicamentos activos por principio activo. */
    public List<Medicamento> buscarPorPrincipioActivo(String principioActivo) {
        return medicamentoRepository
                .findByPrincipiosActivosContainingIgnoreCaseAndActivoTrueOrderByNombreGenericoAsc(
                        principioActivo
                );
    }

    /** Busca un medicamento por ID, esté activo o no. */
    public Medicamento buscarPorId(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Medicamento no encontrado"
                ));
    }

    /** Añade un medicamento al catálogo como activo. */
    @Transactional
    public Medicamento guardar(MedicamentoRequest request) {
        Medicamento medicamento = new Medicamento();
        copiarDatos(request, medicamento);
        medicamento.setActivo(true);

        return medicamentoRepository.save(medicamento);
    }

    /** Actualiza los datos de un medicamento existente. */
    @Transactional
    public Medicamento actualizar(Long id, MedicamentoRequest request) {
        Medicamento medicamento = buscarPorId(id);
        copiarDatos(request, medicamento);

        return medicamentoRepository.save(medicamento);
    }

    /**
     * Desactiva el medicamento sin borrarlo de la base de datos,
     * para conservar las referencias históricas.
     */
    @Transactional
    public void desactivar(Long id) {
        Medicamento medicamento = buscarPorId(id);
        medicamento.setActivo(false);
        medicamentoRepository.save(medicamento);
    }

    /** Copia los campos editables del DTO a la entidad. */
    private void copiarDatos(
            MedicamentoRequest request,
            Medicamento medicamento
    ) {
        medicamento.setNombreGenerico(request.nombreGenerico());
        medicamento.setNombreComercial(request.nombreComercial());
        medicamento.setPrincipiosActivos(request.principiosActivos());
        medicamento.setConcentracion(request.concentracion());
        medicamento.setFormaFarmaceutica(request.formaFarmaceutica());
        medicamento.setPresentacion(request.presentacion());
    }
}