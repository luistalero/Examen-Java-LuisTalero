package com.skeletonhexa.application.usecase;

import com.skeletonhexa.domain.entities.Medico;
import com.skeletonhexa.domain.repository.MedicoRepository;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MedicoUseCase {
    
    private final MedicoRepository medicoRepository;
    
    public MedicoUseCase(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }
    
    public Medico getMedicoById(int id) {
        return medicoRepository.findById(id);
    }
    
    public List<Medico> getAllMedicos() {
        return medicoRepository.findAll();
    }
    
    public List<Medico> getMedicosByEspecialidad(int especialidadId) {
        return medicoRepository.findByEspecialidad(especialidadId);
    }
    
    public Medico getMedicoByNumeroColegiado(String numeroColegiado) {
        if (numeroColegiado == null || numeroColegiado.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de colegiado inválido");
        }
        return medicoRepository.findByNumeroColegiado(numeroColegiado);
    }
    
    public boolean registrarMedico(String nombre, String apellido, int especialidadId, 
                                  String numeroColegiado, String horarioInicio, String horarioFin) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        if (especialidadId <= 0) {
            throw new IllegalArgumentException("Especialidad inválida");
        }
        
        if (numeroColegiado == null || numeroColegiado.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de colegiado inválido");
        }
        
        // Validar formato de hora
        LocalTime horaInicio = null;
        LocalTime horaFin = null;
        
        try {
            if (horarioInicio != null && !horarioInicio.trim().isEmpty()) {
                horaInicio = LocalTime.parse(horarioInicio, DateTimeFormatter.ISO_LOCAL_TIME);
            }
            
            if (horarioFin != null && !horarioFin.trim().isEmpty()) {
                horaFin = LocalTime.parse(horarioFin, DateTimeFormatter.ISO_LOCAL_TIME);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm:ss o HH:mm");
        }
        
        // Verificar si ya existe un médico con el mismo número de colegiado
        Medico existente = medicoRepository.findByNumeroColegiado(numeroColegiado);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un médico con ese número de colegiado");
        }
        
        // Crear y guardar el médico
        Medico medico = new Medico();
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setEspecialidadId(especialidadId);
        medico.setNumeroColegiado(numeroColegiado);
        medico.setHorarioInicio(horaInicio);
        medico.setHorarioFin(horaFin);
        
        return medicoRepository.save(medico);
    }
    
    public boolean actualizarMedico(int id, String nombre, String apellido, int especialidadId, 
                                   String numeroColegiado, String horarioInicio, String horarioFin) {
        // Validaciones similares a registrarMedico
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        
        if (especialidadId <= 0) {
            throw new IllegalArgumentException("Especialidad inválida");
        }
        
        if (numeroColegiado == null || numeroColegiado.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de colegiado inválido");
        }
        
        // Validar formato de hora
        LocalTime horaInicio = null;
        LocalTime horaFin = null;
        
        try {
            if (horarioInicio != null && !horarioInicio.trim().isEmpty()) {
                horaInicio = LocalTime.parse(horarioInicio, DateTimeFormatter.ISO_LOCAL_TIME);
            }
            
            if (horarioFin != null && !horarioFin.trim().isEmpty()) {
                horaFin = LocalTime.parse(horarioFin, DateTimeFormatter.ISO_LOCAL_TIME);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de hora inválido. Use HH:mm:ss o HH:mm");
        }
        
        // Verificar que el médico existe
        Medico medico = medicoRepository.findById(id);
        if (medico == null) {
            throw new IllegalArgumentException("Médico no encontrado");
        }
        
        // Verificar si ya existe otro médico con el mismo número de colegiado
        Medico existente = medicoRepository.findByNumeroColegiado(numeroColegiado);
        if (existente != null && existente.getId() != id) {
            throw new IllegalArgumentException("Ya existe un médico con ese número de colegiado");
        }
        
        // Actualizar el médico
        medico.setNombre(nombre);
        medico.setApellido(apellido);
        medico.setEspecialidadId(especialidadId);
        medico.setNumeroColegiado(numeroColegiado);
        medico.setHorarioInicio(horaInicio);
        medico.setHorarioFin(horaFin);
        
        return medicoRepository.update(medico);
    }
    
    public boolean eliminarMedico(int id) {
        return medicoRepository.delete(id);
    }
}