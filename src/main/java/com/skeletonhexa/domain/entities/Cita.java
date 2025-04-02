package com.skeletonhexa.domain.entities;

import java.time.LocalDateTime;

public class Cita {
    private int id;
    private int pacienteId;
    private int medicoId;
    private LocalDateTime fechaHora;
    private String estado; // pendiente, confirmada, completada, cancelada, no_asistio
    private int duracionMinutos;
    private LocalDateTime fechaCreacion;
    private String notas;
    
    // Constructor, getters y setters
}
