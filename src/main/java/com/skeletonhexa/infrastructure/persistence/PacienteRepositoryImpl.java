package com.skeletonhexa.infrastructure.persistence;

import com.skeletonhexa.domain.entities.Paciente;
import com.skeletonhexa.domain.repository.PacienteRepository;
import com.skeletonhexa.infrastructure.database.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteRepositoryImpl implements PacienteRepository {
    
    private ConnectionDb connection;
    
    public PacienteRepositoryImpl(ConnectionDb connection) {
        this.connection = connection;
    }
    
    @Override
    public Paciente findById(int id) {
        String sql = "SELECT * FROM Paciente WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPaciente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Paciente> findAll() {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM Paciente WHERE activo = 1";
        
        try (Connection conn = connection.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
    
    @Override
    public List<Paciente> findByNombreApellido(String nombre, String apellido) {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM Paciente WHERE nombre LIKE ? AND apellido LIKE ? AND activo = 1";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            stmt.setString(2, "%" + apellido + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                pacientes.add(mapResultSetToPaciente(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pacientes;
    }
    
    @Override
    public Paciente findByDni(String dni) {
        String sql = "SELECT * FROM Paciente WHERE dni = ? AND activo = 1";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPaciente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public Paciente findByEmail(String email) {
        String sql = "SELECT * FROM Paciente WHERE email = ? AND activo = 1";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPaciente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean save(Paciente paciente) {
        String sql = "INSERT INTO Paciente (nombre, apellido, fecha_nacimiento, direccion, email, dni, activo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setString(4, paciente.getDireccion());
            stmt.setString(5, paciente.getEmail());
            stmt.setString(6, paciente.getDni());
            stmt.setBoolean(7, paciente.isActivo());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    paciente.setId(generatedKeys.getInt(1));
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Paciente paciente) {
        String sql = "UPDATE Paciente SET nombre = ?, apellido = ?, fecha_nacimiento = ?, direccion = ?, email = ?, dni = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, paciente.getNombre());
            stmt.setString(2, paciente.getApellido());
            stmt.setDate(3, Date.valueOf(paciente.getFechaNacimiento()));
            stmt.setString(4, paciente.getDireccion());
            stmt.setString(5, paciente.getEmail());
            stmt.setString(6, paciente.getDni());
            stmt.setBoolean(7, paciente.isActivo());
            stmt.setInt(8, paciente.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int id) {
        // Soft delete - just mark as inactive
        String sql = "UPDATE Paciente SET activo = 0 WHERE id = ?";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Paciente mapResultSetToPaciente(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setId(rs.getInt("id"));
        paciente.setNombre(rs.getString("nombre"));
        paciente.setApellido(rs.getString("apellido"));
        paciente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
        paciente.setDireccion(rs.getString("direccion"));
        paciente.setEmail(rs.getString("email"));
        paciente.setDni(rs.getString("dni"));
        paciente.setActivo(rs.getBoolean("activo"));
        
        // If there is a timestamp field, convert it
        Timestamp timestamp = rs.getTimestamp("fecha_registro");
        if (timestamp != null) {
            paciente.setFechaRegistro(timestamp.toLocalDateTime());
        }
        
        return paciente;
    }
}