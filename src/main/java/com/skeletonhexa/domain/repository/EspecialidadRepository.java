package com.skeletonhexa.domain.repository;

import java.util.List;

import com.skeletonhexa.domain.entities.Especialidad;

public interface EspecialidadRepository {
    List<Especialidad> listarEspecialidades();
    Especialidad buscarPorId(int id);
    boolean guardar(Especialidad especialidad);
    boolean actualizar(Especialidad especialidad);
    boolean eliminar(int id);
}
