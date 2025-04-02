package com.skeletonhexa.application.usecase;

import com.skeletonhexa.domain.entities.Paciente;
import com.skeletonhexa.domain.repository.PacienteRepository;
import com.skeletonhexa.domain.validation.InputValidator;

import java.time.LocalDate;
import java.util.List;

public class PacienteUseCase {
    
    private final PacienteRepository pacienteRepository;
    
    public PacienteUseCase(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }
    
    public Paciente getPacienteById(int id) {
        return pacienteRepository.findById(id);
    }
    
    public List<Paciente> getAllPacientes() {
        return pacienteRepository.findAll();
    }
    
    public List<Paciente> buscarPacientesPorNombre(String nombre, String apellido) {
        return pacienteRepository.findByNombreApellido(nombre, apellido);
    }
    
    public Paciente buscarPacientePorDni(String dni) {
        if (!InputValidator.isValidDNI(dni)) {
            throw new IllegalArgumentException("DNI inválido");
        }
        return pacienteRepository.findByDni(dni);
    }
    
    public Paciente buscarPacientePorEmail(String email) {
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        return pacienteRepository.findByEmail(email);
    }
    
    public boolean registrarPaciente(String nombre, String apellido, String fechaNacimiento, 
                                     String direccion, String email, String dni) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        if (!InputValidator.isValidDate(fechaNacimiento)) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd o dd/MM/yyyy");
        }
        
        LocalDate fechaNac = InputValidator.parseDate(fechaNacimiento);
        
        if (email != null && !email.trim().isEmpty() && !InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        if (dni != null && !dni.trim().isEmpty() && !InputValidator.isValidDNI(dni)) {
            throw new IllegalArgumentException("DNI inválido");
        }
        
        // Verificar si ya existe un paciente con el mismo DNI o email
        if (dni != null && !dni.trim().isEmpty()) {
            Paciente existente = pacienteRepository.findByDni(dni);
            if (existente != null) {
                throw new IllegalArgumentException("Ya existe un paciente con ese DNI");
            }
        }
        
        if (email != null && !email.trim().isEmpty()) {
            Paciente existente = pacienteRepository.findByEmail(email);
            if (existente != null) {
                throw new IllegalArgumentException("Ya existe un paciente con ese email");
            }
        }
        
        // Crear y guardar el paciente
        Paciente paciente = new Paciente();
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setFechaNacimiento(fechaNac);
        paciente.setDireccion(direccion);
        paciente.setEmail(email);
        paciente.setDni(dni);
        
        return pacienteRepository.save(paciente);
    }
    
    public boolean actualizarPaciente(int id, String nombre, String apellido, String fechaNacimiento, 
                                      String direccion, String email, String dni) {
        // Validaciones similares a registrarPaciente
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        if (!InputValidator.isValidDate(fechaNacimiento)) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd o dd/MM/yyyy");
        }
        
        LocalDate fechaNac = InputValidator.parseDate(fechaNacimiento);
        
        if (email != null && !email.trim().isEmpty() && !InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        if (dni != null && !dni.trim().isEmpty() && !InputValidator.isValidDNI(dni)) {
            throw new IllegalArgumentException("DNI inválido");
        }
        
        // Verificar que el paciente existe
        Paciente paciente = pacienteRepository.findById(id);
        if (paciente == null) {
            throw new IllegalArgumentException("Paciente no encontrado");
        }
        
        // Verificar si ya existe otro paciente con el mismo DNI o email
        if (dni != null && !dni.trim().isEmpty()) {
            Paciente existente = pacienteRepository.findByDni(dni);
            if (existente != null && existente.getId() != id) {
                throw new IllegalArgumentException("Ya existe un paciente con ese DNI");
            }
        }
        
        if (email != null && !email.trim().isEmpty()) {
            Paciente existente = pacienteRepository.findByEmail(email);
            if (existente != null && existente.getId() != id) {
                throw new IllegalArgumentException("Ya existe un paciente con ese email");
            }
        }
        
        // Actualizar el paciente
        paciente.setNombre(nombre);
        paciente.setApellido(apellido);
        paciente.setFechaNacimiento(fechaNac);
        paciente.setDireccion(direccion);
        paciente.setEmail(email);
        paciente.setDni(dni);
        
        return pacienteRepository.update(paciente);
    }
    
    public boolean eliminarPaciente(int id) {
        return pacienteRepository.delete(id);
    }
}