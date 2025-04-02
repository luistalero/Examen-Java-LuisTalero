package com.skeletonhexa.domain.repository;

import java.time.LocalDate;
import java.util.List;

import com.skeletonhexa.domain.entities.Cita;

public interface CitaRepository {
    List<Cita> buscarPorPaciente(int pacienteId);
    List<Cita> buscarPorMedico(int medicoId);
    List<Cita> buscarPorFecha(int medicoId, LocalDate fecha);
    Cita buscarPorId(int id);
    boolean guardar(Cita cita);
    boolean actualizar(Cita cita);
    boolean cambiarEstado(int id, String nuevoEstado);
    boolean eliminar(int id);
}