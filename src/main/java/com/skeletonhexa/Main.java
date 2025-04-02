package com.skeletonhexa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import com.skeletonhexa.infrastructure.adapter.ui.UiAuth;
import com.skeletonhexa.domain.validation.InputValidator;
import com.skeletonhexa.infrastructure.adapter.ui.Manageable;
import com.skeletonhexa.infrastructure.adapter.ui.UiHome;
import com.skeletonhexa.infrastructure.database.ConnectMysqlFactory;
import com.skeletonhexa.infrastructure.database.ConnectionDb;

public class Main {
    private static final String CONNECTION_SUCCESS_MSG = """
        =============================================================
        |   CONEXIÓN A LA BASE DE DATOS ESTABLECIDA CORRECTAMENTE   |
        =============================================================
        """;
        
    private static final String CONNECTION_FAIL_MSG = """
        ===================================================        
        |    ERROR AL CONECTAR A LA BASE DE DATOS         |
        ===================================================
        """;
        
    private static final String EXIT_MSG = "Gracias por usar el sistema";

    public static void main(String[] args) {
        try {
            // Usamos tu factory para crear la conexión
            ConnectionDb conexionDB = ConnectMysqlFactory.crearConexion();
            Connection conexion = conexionDB.getConexion();
            
            if (conexion == null || conexion.isClosed()) {
                System.err.println(CONNECTION_FAIL_MSG);
                return;
            }
            // Mostramos mensaje de conexión exitosa
            System.out.println(CONNECTION_SUCCESS_MSG);
            InputValidator.pause(1500);
            InputValidator.clearConsole();
            
            // Manejo del flujo principal
            handleMainFlow(conexion);
            
        } catch (SQLException e) {
            System.err.println(CONNECTION_FAIL_MSG);
            System.err.println("Error: " + e.getMessage());
        } finally {
            System.out.println(EXIT_MSG);
        }
    }

    private static void handleMainFlow(Connection conexion) {
        Scanner sc = new Scanner(System.in);
        UiAuth authUI = new UiAuth(sc, conexion);

        while (true) {
            int authOption = authUI.showAuthMenu();
            InputValidator.clearConsole();

            switch (authOption) {
                case 1:
                    if (handleLogin(authUI, conexion, sc)) {
                        continue;
                    }
                    break;
                case 2:
                    handleRegistration(authUI);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Opción no válida.");
            }

            InputValidator.pause(1500);
            InputValidator.clearConsole();
        }
    }

    private static boolean handleLogin(UiAuth authUI, Connection conexion, Scanner sc) {
        String[] loginData = authUI.handleLogin();
        if (authUI.authenticate(loginData[0], loginData[1])) {
            System.out.println("¡Bienvenido " + authUI.getCurrentUserName() + " (" +
                    authUI.getCurrentUserType().toUpperCase() + ")!");
            InputValidator.pause(1000);
            InputValidator.clearConsole();
            showUserMenu(conexion, sc, authUI); // Pasar la conexión a showUserMenu
            return true;
        }
        System.out.println("Credenciales incorrectas. Intente nuevamente.");
        return false;
    }

    private static void handleRegistration(UiAuth authUI) {
        String[] registerData = authUI.handleRegister();
        if (authUI.registerUser(registerData)) { // Cambiado a registerUser()
            System.out.println("¡Registro exitoso! Por favor inicie sesión.");
        } else {
            System.out.println("Error en el registro. Intente nuevamente.");
        }
    }

    private static void showUserMenu(Connection conexion, Scanner sc, UiAuth authUI) {
        Manageable menu = UiHome.getMenu(authUI.getCurrentUserType(), conexion, sc, authUI);

        if (menu != null) {
            boolean logoutRequested = false;
            while (!logoutRequested) {
                InputValidator.clearConsole();
                logoutRequested = menu.Manage(sc);
                if (!logoutRequested) {
                    System.out.println("\nPresione Enter para continuar...");
                    sc.nextLine();
                }
            }
        } else {
            System.out.println("No hay menú disponible para tu tipo de usuario (" +
                    authUI.getCurrentUserType() + ")");
            InputValidator.pause(2000);
        }
    }

    
}