package com.skeletonhexa.domain.validation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;


public class InputValidator {
    public static String validateText(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Field cannot be empty");
            } else {
                return input;
            }
        }
    }
    public static int validateInteger(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Must enter a valid integer");
            }
        }
    }
    public static double validateDecimal(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Must enter a valid decimal number");
            }
        }
    }
    public static String validateEmail(Scanner sc, String message) {
        while (true) {
            String email = validateText(sc, message);
            if (email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                return email;
            }
            System.out.println("Error: Invalid email format (example: user@domain.com)");
        }
    }
    public static String validatePassword(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String password = sc.nextLine().trim();
            if (password.length() >= 8) {
                return password;
            }
            System.out.println("Password must be at least 8 characters long.");
        }
    }

    public static String validatePhone(Scanner sc, String message) {
        while (true) {
            String phone = validateText(sc, message);
            if (phone.matches("^[0-9]{10,15}$")) {
                return phone;
            }
            System.out.println("Error: Invalid phone format (must contain 10-15 digits)");
        }
    }

    public static boolean validateConfirmation(Scanner sc, String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim().toLowerCase();
            if (input.equals("s") || input.equals("si") || input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            }
            System.out.println("Please enter S/N to confirm.");
        }
    }
    public static int validateIntegerMin(Scanner sc, String message, int min) {
        while (true) {
            int value = validateInteger(sc, message);
            if (value >= min) {
                return value;
            }
            System.out.printf("Error: Value must be greater than or equal to %d\n", min);
        }
    }

    public static int validateIntegerMinMax(Scanner sc, String message, int min, int max) {
        while (true) {
            int value = validateInteger(sc, message);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.printf("Error: Value must be between %d and %d\n", min, max);
        }
    }
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
    /**
     * Pausa la ejecución del hilo por un número de milisegundos.
     * @param milliseconds El número de milisegundos para pausar.
     */
    public static void pause(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Pause interrupted");
        }
    }

    /**
     * Valida que el nombre del producto no sea nulo, vacío y no exceda 255 caracteres.
     * @param name El nombre del producto a validar.
     * @return true si el nombre es válido, false si no lo es.
     */
    public static boolean validateProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() <= 255;
    }

    /**
     * Valida que el precio del producto sea no negativo.
     * @param price El precio del producto a validar.
     * @return true si el precio es válido, false si no lo es.
     */
    public static boolean validateProductPrice(double price) {
        return price >= 0;
    }

    /**
     * Valida que el stock del producto sea no negativo.
     * @param stock El stock del producto a validar.
     * @return true si el stock es válido, false si no lo es.
     */
    public static boolean validateProductStock(int stock) {
        return stock >= 0;
    }

    /**
     * Valida que el stock mínimo del producto sea no negativo.
     * @param minStock El stock mínimo del producto a validar.
     * @return true si el stock mínimo es válido, false si no lo es.
     */
    public static boolean validateProductMinStock(int minStock) {
        return minStock >= 0;
    }

    /**
     * Valida que el nombre del usuario no sea nulo, vacío y no exceda 255 caracteres.
     * @param name El nombre del usuario a validar.
     * @return true si el nombre es válido, false si no lo es.
     */
    public static boolean validateUserName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() <= 255;
    }

    /**
     * Valida que el rol del usuario no sea nulo, vacío y no exceda 50 caracteres.
     * @param role El rol del usuario a validar.
     * @return true si el rol es válido, false si no lo es.
     */
    public static boolean validateUserRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return false;
        }
        return role.length() <= 50;
    }

    /**
     * Valida que el correo electrónico del usuario tenga un formato válido.
     * @param email El correo electrónico del usuario a validar.
     * @return true si el correo electrónico es válido, false si no lo es.
     */
    public static boolean validateUserEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * Valida que la contraseña del usuario tenga al menos 8 caracteres.
     * @param password La contraseña del usuario a validar.
     * @return true si la contraseña es válida, false si no lo es.
     */
    public static boolean validateUserPassword(String password) {
        return password != null && password.length() >= 8;
    }

    /**
     * Valida que el nombre de la sucursal no sea nulo, vacío y no exceda 255 caracteres.
     * @param name El nombre de la sucursal a validar.
     * @return true si el nombre es válido, false si no lo es.
     */
    public static boolean validateBranchName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.length() <= 255;
    }

    /**
     * Valida que la dirección de la sucursal no sea nula, vacía y no exceda 255 caracteres.
     * @param address La dirección de la sucursal a validar.
     * @return true si la dirección es válida, false si no lo es.
     */
    public static boolean validateBranchAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }
        return address.length() <= 255;
    }

    /**
     * Valida que el ID sea un entero positivo.
     * @param id El ID a validar.
     * @return true si el ID es válido, false si no lo es.
     */
    public static boolean validateId(int id) {
        return id > 0;
    }

    public static int validateBranchId(Scanner sc, String message, Connection connection) {
        while (true) {
            int branchId = validateInteger(sc, message);
            if (branchId > 0 && branchIdExists(connection, branchId)) {
                return branchId;
            }
            System.out.println("Error: Branch ID must be a positive integer and exist in the database.");
        }
    }

    /**
     * Verifica si un branchId existe en la tabla Branches de la base de datos.
     * @param connection La conexión a la base de datos.
     * @param branchId El branchId a verificar.
     * @return true si el branchId existe, false si no.
     */
    public static boolean branchIdExists(Connection connection, int branchId) {
        String sql = "SELECT 1 FROM Branch WHERE id_branch = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, branchId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores en producción
            return false;
        }
    }
    public static boolean emailExists(Connection connection, String email) throws SQLException {
        String sql = "SELECT 1 FROM Auth WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    /**
     * Valida que el ID de rol esté entre 1 y 3.
     * @param roleId El ID de rol a validar.
     * @return true si el rol es válido, false si no lo es.
     */
    public static boolean validateRoleId(int roleId) {
        return roleId >= 1 && roleId <= 3;
    }
    
    /**
     * Valida que el ID de rol esté entre 1 y 3 usando Scanner.
     * @param sc El objeto Scanner para leer la entrada del usuario.
     * @param message El mensaje para mostrar al usuario.
     * @return El ID de rol validado.
     */
    public static int validateRoleId(Scanner sc, String message) {
        return validateIntegerMinMax(sc, message, 1, 3);
    }
    public static double validateDecimalMin(Scanner sc, String prompt, double min) {
        double input;
        while (true) {
            System.out.print(prompt);
            try {
                input = sc.nextDouble();
                if (input >= min) {
                    return input;
                } else {
                    System.out.println("Error: Value must be at least " + min + ". Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: Invalid input. Please enter a decimal number.");
                sc.next(); // Clear the invalid input
            }
        }
    }  
    
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
    
    // Validate DNI format (Spanish-like format)
    public static boolean isValidDNI(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        return dni.matches("\\d{8}[A-Za-z]") || dni.matches("\\d{8}");
    }
    
    // Validate date format
    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
            return true;
        } catch (DateTimeParseException e) {
            try {
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return true;
            } catch (DateTimeParseException e2) {
                return false;
            }
        }
    }
    
    // Convert string to LocalDate
    public static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            try {
                return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Invalid date format. Use yyyy-MM-dd or dd/MM/yyyy");
            }
        }
    }
    
    // Validate phone number
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return phone.matches("\\d{9}") || phone.matches("\\+\\d{1,3}\\s?\\d{9}");
    }
    
    // Validate password (at least 8 chars, 1 uppercase, 1 lowercase, 1 number)
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasNumber = password.matches(".*\\d.*");
        
        return hasUppercase && hasLowercase && hasNumber;
    }
}