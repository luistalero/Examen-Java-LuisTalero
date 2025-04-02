package com.skeletonhexa.domain.entities;

import java.time.LocalTime;

public class Medico {
    private int id;
    private String nombre;
    private String apellido;
    private int especialidadId;
    private String numeroColegiado;
    private LocalTime horarioInicio;
    private LocalTime horarioFin;
    private boolean activo;
    
    // Constructor
    public Medico() {
        this.activo = true;
    }
    
    public Medico(int id, String nombre, String apellido, int especialidadId, 
                 String numeroColegiado, LocalTime horarioInicio, LocalTime horarioFin) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.especialidadId = especialidadId;
        this.numeroColegiado = numeroColegiado;
        this.horarioInicio = horarioInicio;
        this.horarioFin = horarioFin;
        this.activo = true;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public int getEspecialidadId() {
        return especialidadId;
    }
    
    public void setEspecialidadId(int especialidadId) {
        this.especialidadId = especialidadId;
    }
    
    public String getNumeroColegiado() {
        return numeroColegiado;
    }
    
    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }
    
    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }
    
    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }
    
    public LocalTime getHorarioFin() {
        return horarioFin;
    }
    
    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    @Override
    public String toString() {
        return "Medico{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", especialidadId=" + especialidadId +
                ", numeroColegiado='" + numeroColegiado + '\'' +
                ", activo=" + activo +
                '}';
    }
}