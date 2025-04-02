package com.skeletonhexa.domain.repository;

import com.skeletonhexa.domain.entities.Medico;
import java.util.List;

public interface MedicoRepository {
    Medico findById(int id);
    List<Medico> findAll();
    List<Medico> findByEspecialidad(int especialidadId);
    Medico findByNumeroColegiado(String numeroColegiado);
    boolean save(Medico medico);
    boolean update(Medico medico);
    boolean delete(int id);
}