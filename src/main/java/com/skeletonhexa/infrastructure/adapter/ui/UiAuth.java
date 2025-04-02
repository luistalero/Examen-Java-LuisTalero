package com.skeletonhexa.infrastructure.adapter.ui;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.skeletonhexa.domain.validation.InputValidator;

public class UiAuth {
    private final Connection connection;
    private String currentUserType;
    private int currentUserId;
    private String currentUserName;
    private final Scanner sc;

    public UiAuth(Scanner sc, Connection connection) {
        this.sc = sc;
        this.connection = connection;
    }

    public int showAuthMenu() {
        System.out.println("""
                ==============================
                |        ACCESS MENU         |
                ==============================
                | 1. Login                   |
                | 2. Register                |
                | 3. Exit                    |
                ==============================
                """);
        return InputValidator.validateInteger(sc, "Select an option: ");
    }

    public String[] handleLogin() {
        String email;
        String password;
        do {
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            if (!InputValidator.validateUserEmail(email)) {
                System.out.println("Error: Formato de email inválido (ejemplo: usuario@dominio.com)");
            }
        } while (!InputValidator.validateUserEmail(email));
    
        do {
            System.out.print("Password: ");
            password = sc.nextLine();
            if (!InputValidator.validateUserPassword(password)) {
                System.out.println("Error: La contraseña debe tener al menos 8 caracteres");
            }
        } while (!InputValidator.validateUserPassword(password));
        
        return new String[]{email, password};
    }

    public void logout() {
        this.currentUserType = null;
        this.currentUserId = 0;
        this.currentUserName = null;

        System.out.println("Logout successful.");
        InputValidator.pause(1000);
    }

    public boolean authenticate(String email, String password) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT u.id, u.email, r.nombre as rol, " +
                "COALESCE(p.nombre, m.nombre) as nombre, " +
                "COALESCE(p.apellido, m.apellido) as apellido " +
                "FROM Usuario u " +
                "JOIN Rol r ON u.rol_id = r.id " +
                "LEFT JOIN Usuario_Paciente up ON u.id = up.usuario_id " +
                "LEFT JOIN Paciente p ON up.paciente_id = p.id " +
                "LEFT JOIN Usuario_Medico um ON u.id = um.usuario_id " +
                "LEFT JOIN Medico m ON um.medico_id = m.id " +
                "WHERE u.email = ? AND u.contrasena = ? AND u.activo = TRUE")) {
    
            stmt.setString(1, email);
            stmt.setString(2, password); // En producción, usa hash
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    currentUserType = rs.getString("rol");
                    currentUserId = rs.getInt("id");
                    currentUserName = rs.getString("nombre") + " " + rs.getString("apellido");
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return false;
    }

    public String[] handleRegister() {
        String name = InputValidator.validateText(sc, "Full name: ");
        String email = InputValidator.validateEmail(sc, "Email: ");
        String phone = InputValidator.validatePhone(sc, "Phone: ");
        String password = InputValidator.validatePassword(sc, "Password: ");
        
        return new String[]{name, email, phone, password, "3"}; 
    }

    public boolean registerUser(String[] registerData) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement customerStmt = connection.prepareStatement(
                    "INSERT INTO Customer (name, phone, id_role) VALUES (?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS)) {

                customerStmt.setString(1, registerData[0]);
                customerStmt.setString(2, registerData[2]);
                customerStmt.setInt(3, Integer.parseInt(registerData[4]));
                customerStmt.executeUpdate();

                try (ResultSet rs = customerStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int customerId = rs.getInt(1);

                        try (PreparedStatement authStmt = connection.prepareStatement(
                                "INSERT INTO Auth (email, password_hash, id_customer) VALUES (?, ?, ?)")) {

                            authStmt.setString(1, registerData[1]);
                            authStmt.setString(2, registerData[3]);
                            authStmt.setInt(3, customerId);
                            authStmt.executeUpdate();
                        }
                    }
                }
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.err.println("Rollback error: " + ex.getMessage());
            }
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUserType() {
        return currentUserType;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }
}