package com.skeletonhexa.infrastructure.adapter.ui;

import java.sql.Connection;
import java.util.Scanner;

import com.skeletonhexa.application.usecase.MedicoUseCase;
import com.skeletonhexa.domain.validation.InputValidator;
import com.skeletonhexa.infrastructure.persistence.MedicoRepositoryImpl;

public class UiMedico implements Manageable {
    private final Connection connection;
    private final Scanner sc;
    private final UiAuth authUI;
    private final MedicoUseCase medicoUseCase;
    
    public UiMedico(Connection connection, Scanner sc, UiAuth authUI) {
        this.connection = connection;
        this.sc = sc;
        this.authUI = authUI;
        this.medicoUseCase = new MedicoUseCase(new MedicoRepositoryImpl(connection));
    }
    
    @Override
    public boolean Manage(Scanner sc) {
        System.out.println("\n===== MENÚ MÉDICO =====");
        System.out.println("1. Ver mis datos");
        System.out.println("2. Ver mi horario");
        System.out.println("3. Ver mis citas del día");
        System.out.println("4. Gestionar disponibilidad");
        System.out.println("5. Ver historial de paciente");
        System.out.println("0. Cerrar sesión");
        
        int option = InputValidator.validateInteger(sc, "Seleccione una opción: ");
        
        switch (option) {
            case 1: verDatosMedico(); break;
            case 2: verHorario(); break;
            case 3: verCitasDia(); break;
            case 4: gestionarDisponibilidad(); break;
            case 5: verHistorialPaciente(); break;
            case 0: 
                authUI.logout();
                return true;
            default:
                System.out.println("Opción no válida");
        }
        return false;
    }

    private void verHistorialPaciente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verHistorialPaciente'");
    }

    private void gestionarDisponibilidad() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'gestionarDisponibilidad'");
    }

    private void verCitasDia() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verCitasDia'");
    }

    private void verDatosMedico() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verDatosMedico'");
    }

    private void verHorario() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verHorario'");
    }
    
    // Implementa los métodos para cada opción...
}