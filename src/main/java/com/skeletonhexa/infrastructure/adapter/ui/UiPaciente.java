package com.skeletonhexa.infrastructure.adapter.ui;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import com.skeletonhexa.application.usecase.PacienteUseCase;
import com.skeletonhexa.domain.entities.Cita;
import com.skeletonhexa.domain.entities.Especialidad;
import com.skeletonhexa.domain.entities.Medico;
import com.skeletonhexa.domain.validation.InputValidator;
import com.skeletonhexa.infrastructure.persistence.PacienteRepositoryImpl;

public class UiPaciente implements Manageable {
    private final Connection connection;
    private final Scanner sc;
    private final UiAuth authUI;
    private final PacienteUseCase pacienteUseCase;
    
    public UiPaciente(Connection connection, Scanner sc, UiAuth authUI) {
        this.connection = connection;
        this.sc = sc;
        this.authUI = authUI;
        this.pacienteUseCase = new PacienteUseCase(new PacienteRepositoryImpl(connection));
    }
    
    @Override
    public boolean Manage(Scanner sc) {
        System.out.println("\n===== MENÚ PACIENTES =====");
        System.out.println("1. Ver mi información");
        System.out.println("2. Actualizar mis datos");
        System.out.println("3. Agendar cita");
        System.out.println("4. Ver mis citas");
        System.out.println("5. Cambiar contraseña");
        System.out.println("0. Cerrar sesión");
        
        int option = InputValidator.validateInteger(sc, "Seleccione una opción: ");
        
        switch (option) {
            case 1: mostrarInformacionPaciente(); break;
            case 2: actualizarDatosPaciente(); break;
            case 3: agendarCita(); break;
            case 4: verCitas(); break;
            case 5: cambiarContrasena(); break;
            case 6: obtenerPacienteIdPorUsuario(); break;
            case 0: 
                authUI.logout();
                return true;
            default:
                System.out.println("Opción no válida");
        }
        return false;
    }

    private void cambiarContrasena() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cambiarContrasena'");
    }

    private void verCitas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verCitas'");
    }

    private void agendarCita() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'agendarCita'");
    }

    private void actualizarDatosPaciente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarDatosPaciente'");
    }

    private void mostrarInformacionPaciente() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostrarInformacionPaciente'");
    }

    private int obtenerPacienteIdPorUsuario(int currentUserId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerPacienteIdPorUsuario'");
    }

    private void verHistorialCitas() {
        // Obtener el ID del paciente actual
        int pacienteId = obtenerPacienteIdPorUsuario(authUI.getCurrentUserId());
        
        // Obtener todas las citas del paciente
        List<Cita> citas = citaUseCase.obtenerHistorialCitas(pacienteId);
        
        if (citas.isEmpty()) {
            System.out.println("No tiene citas registradas.");
            return;
        }
        
        System.out.println("\n===== HISTORIAL DE CITAS =====");
        System.out.printf("%-5s %-15s %-20s %-15s %-15s\n", "ID", "FECHA", "MÉDICO", "ESPECIALIDAD", "ESTADO");
        
        for (Cita cita : citas) {
            Medico medico = medicoUseCase.buscarPorId(cita.getMedicoId());
            Especialidad especialidad = especialidadUseCase.buscarPorId(medico.getEspecialidadId());
            
            System.out.printf("%-5d %-15s %-20s %-15s %-15s\n", 
                cita.getId(),
                cita.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                medico.getNombre() + " " + medico.getApellido(),
                especialidad.getNombre(),
                cita.getEstado());
        }
    }
    // Implementa los métodos para cada opción...

    
}