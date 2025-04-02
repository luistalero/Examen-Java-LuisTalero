package com.skeletonhexa.domain.repository;

import com.skeletonhexa.domain.entities.Paciente;
import java.util.List;

public interface PacienteRepository {
    Paciente findById(int id);
    List<Paciente> findAll();
    List<Paciente> findByNombreApellido(String nombre, String apellido);
    Paciente findByDni(String dni);
    Paciente findByEmail(String email);
    boolean save(Paciente paciente);
    boolean update(Paciente paciente);
    boolean delete(int id);
}