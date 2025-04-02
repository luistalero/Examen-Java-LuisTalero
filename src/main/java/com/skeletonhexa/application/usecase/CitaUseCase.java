package com.skeletonhexa.application.usecase;

import java.time.LocalDateTime;

import com.skeletonhexa.domain.repository.CitaRepository;
import com.skeletonhexa.domain.repository.MedicoRepository;
import com.skeletonhexa.domain.repository.PacienteRepository;

public class CitaUseCase {
    private final CitaRepository citaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    
    public CitaUseCase(CitaRepository citaRepository, MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.citaRepository = citaRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }
    
    public boolean agendarCita(int pacienteId, int medicoId, LocalDateTime fechaHora, int duracionMinutos) {
        
    }
    
}