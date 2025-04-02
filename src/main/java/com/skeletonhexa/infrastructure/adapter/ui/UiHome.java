package com.skeletonhexa.infrastructure.adapter.ui;

import java.sql.Connection;
import java.util.Scanner;

public class UiHome {
    
    // Método estático para obtener el menú adecuado según el tipo de usuario
    public static Manageable getMenu(String userType, Connection connection, Scanner sc, UiAuth authUI) {
        if (userType == null) {
            return null;
        }
        
        // Detectar el tipo de usuario y devolver el menú correspondiente
        switch (userType.toLowerCase()) {
            case "admin":
                return new UiAdmin(connection, sc, authUI);
            case "médico":
            case "medico":
                return new UiMedico(connection, sc, authUI);
            case "paciente":
                return new UiPaciente(connection, sc, authUI);
            default:
                System.out.println("Tipo de usuario no reconocido: " + userType);
                return null;
        }
    }
}