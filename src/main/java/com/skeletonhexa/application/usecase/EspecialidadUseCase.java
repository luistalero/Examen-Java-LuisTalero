package com.skeletonhexa.application.usecase;

import com.skeletonhexa.domain.repository.EspecialidadRepository;

public class EspecialidadUseCase {
    private final EspecialidadRepository especialidadRepository;
    
    public EspecialidadUseCase(EspecialidadRepository especialidadRepository) {
        this.especialidadRepository = especialidadRepository;
    }
    
    // Métodos para manejar la lógica de negocio de especialidades
}
