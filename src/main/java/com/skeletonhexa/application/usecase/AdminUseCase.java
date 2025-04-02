package com.skeletonhexa.application.usecase;

import com.skeletonhexa.domain.entities.Admin;
import com.skeletonhexa.domain.repository.AdminRepository;
import com.skeletonhexa.domain.validation.InputValidator;

public class AdminUseCase {
    
    private final AdminRepository adminRepository;
    
    public AdminUseCase(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    
    public Admin getAdminById(int id) {
        return adminRepository.findById(id);
    }
    
    public Admin getAdminByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username inválido");
        }
        return adminRepository.findByUsername(username);
    }
    
    public Admin getAdminByEmail(String email) {
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        return adminRepository.findByEmail(email);
    }
    
    public boolean authenticateAdmin(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username inválido");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password inválido");
        }
        
        return adminRepository.authenticate(username, password);
    }
    
    public boolean registrarAdmin(String username, String password, String email) {
        // Validaciones
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("El password no puede estar vacío");
        }
        
        if (!InputValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("Password inválido. Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
        }
        
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        // Verificar si ya existe un admin con el mismo username o email
        Admin existenteUsername = adminRepository.findByUsername(username);
        if (existenteUsername != null) {
            throw new IllegalArgumentException("Ya existe un administrador con ese username");
        }
        
        Admin existenteEmail = adminRepository.findByEmail(email);
        if (existenteEmail != null) {
            throw new IllegalArgumentException("Ya existe un administrador con ese email");
        }
        
        // Crear y guardar el admin
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password); // En un caso real, hashearíamos la contraseña
        admin.setEmail(email);
        
        return adminRepository.save(admin);
    }
    
    public boolean actualizarAdmin(int id, String username, String password, String email, boolean active) {
        // Validaciones
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El username no puede estar vacío");
        }
        
        if (password != null && !password.trim().isEmpty() && !InputValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("Password inválido. Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
        }
        
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        // Verificar que el admin existe
        Admin admin = adminRepository.findById(id);
        if (admin == null) {
            throw new IllegalArgumentException("Administrador no encontrado");
        }
        
        // Verificar si ya existe otro admin con el mismo username o email
        Admin existenteUsername = adminRepository.findByUsername(username);
        if (existenteUsername != null && existenteUsername.getId() != id) {
            throw new IllegalArgumentException("Ya existe un administrador con ese username");
        }
        
        Admin existenteEmail = adminRepository.findByEmail(email);
        if (existenteEmail != null && existenteEmail.getId() != id) {
            throw new IllegalArgumentException("Ya existe un administrador con ese email");
        }
        
        // Actualizar el admin
        admin.setUsername(username);
        if (password != null && !password.trim().isEmpty()) {
            admin.setPassword(password); // En un caso real, hashearíamos la contraseña
        }
        admin.setEmail(email);
        admin.setActive(active);
        
        return adminRepository.update(admin);
    }
    
    public boolean eliminarAdmin(int id) {
        return adminRepository.delete(id);
    }
}