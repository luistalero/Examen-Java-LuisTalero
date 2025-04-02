package com.skeletonhexa.infrastructure.persistence;

import com.skeletonhexa.domain.entities.Medico;
import com.skeletonhexa.domain.repository.MedicoRepository;
import com.skeletonhexa.infrastructure.database.ConnectionDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicoRepositoryImpl implements MedicoRepository {
    
    private ConnectionDb connection;
    
    public MedicoRepositoryImpl(ConnectionDb connection) {
        this.connection = connection;
    }
    
    @Override
    public Medico findById(int id) {
        String sql = "SELECT * FROM Medico WHERE id = ?";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMedico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<Medico> findAll() {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM Medico WHERE activo = 1";
        
        try (Connection conn = connection.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                medicos.add(mapResultSetToMedico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicos;
    }
    
    @Override
    public List<Medico> findByEspecialidad(int especialidadId) {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM Medico WHERE especialidad_id = ? AND activo = 1";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, especialidadId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                medicos.add(mapResultSetToMedico(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medicos;
    }
    
    @Override
    public Medico findByNumeroColegiado(String numeroColegiado) {
        String sql = "SELECT * FROM Medico WHERE numero_colegiado = ? AND activo = 1";
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroColegiado);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToMedico(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public boolean save(Medico medico) {
        String sql = "INSERT INTO Medico (nombre, apellido, especialidad_id, numero_colegiado, horario_inicio, horario_fin, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setInt(3, medico.getEspecialidadId());
            stmt.setString(4, medico.getNumeroColegiado());
            
            // Convert LocalTime to Time
            if (medico.getHorarioInicio() != null) {
                stmt.setTime(5, Time.valueOf(medico.getHorarioInicio()));
            } else {
                stmt.setNull(5, Types.TIME);
            }
            
            if (medico.getHorarioFin() != null) {
                stmt.setTime(6, Time.valueOf(medico.getHorarioFin()));
            } else {
                stmt.setNull(6, Types.TIME);
            }
            
            stmt.setBoolean(7, medico.isActivo());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    medico.setId(generatedKeys.getInt(1));
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
    public boolean update(Medico medico) {
        String sql = "UPDATE Medico SET nombre = ?, apellido = ?, especialidad_id = ?, " +
                     "numero_colegiado = ?, horario_inicio = ?, horario_fin = ?, activo = ? WHERE id = ?";
        
        try (Connection conn = connection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, medico.getNombre());
            stmt.setString(2, medico.getApellido());
            stmt.setInt(3, medico.getEspecialidadId());
            stmt.setString(4, medico.getNumeroColegiado());
            
            // Convert LocalTime to Time
            if (medico.getHorarioInicio() != null) {
                stmt.setTime(5, Time.valueOf(medico.getHorarioInicio()));
            } else {
                stmt.setNull(5, Types.TIME);
            }
            
            if (medico.getHorarioFin() != null) {
                stmt.setTime(6, Time.valueOf(medico.getHorarioFin()));
            } else {
                stmt.setNull(6, Types.TIME);
            }
            
            stmt.setBoolean(7, medico.isActivo());
            stmt.setInt(8, medico.getId());
            
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
        String sql = "UPDATE Medico SET activo = 0 WHERE id = ?";
        
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
    
    private Medico mapResultSetToMedico(ResultSet rs) throws SQLException {
        Medico medico = new Medico();
        medico.setId(rs.getInt("id"));
        medico.setNombre(rs.getString("nombre"));
        medico.setApellido(rs.getString("apellido"));
        medico.setEspecialidadId(rs.getInt("especialidad_id"));
        medico.setNumeroColegiado(rs.getString("numero_colegiado"));
        
        // Handle Time conversion to LocalTime
        Time timeInicio = rs.getTime("horario_inicio");
        if (timeInicio != null) {
            medico.setHorarioInicio(timeInicio.toLocalTime());
        }
        
        Time timeFin = rs.getTime("horario_fin");
        if (timeFin != null) {
            medico.setHorarioFin(timeFin.toLocalTime());
        }
        
        medico.setActivo(rs.getBoolean("activo"));
        
        return medico;
    }
}