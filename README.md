```sql
CREATE DATABASE IF NOT EXISTS medical;
USE medical;


CREATE TABLE Rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE COMMENT 'Ej: Admin, Médico, Paciente, Recepcionista',
    descripcion TEXT
);


CREATE TABLE Permiso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE COMMENT 'Ej: crear_cita, ver_historial',
    descripcion TEXT
);

CREATE TABLE Admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    active BOOLEAN DEFAULT TRUE
);


CREATE TABLE Rol_Permiso (
    rol_id INT,
    permiso_id INT,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES Permiso(id) ON DELETE CASCADE
);


CREATE TABLE Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL COMMENT 'Almacenada con hash',
    rol_id INT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE SET NULL
);


CREATE TABLE Especialidad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);


CREATE TABLE Medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    especialidad_id INT NOT NULL,
    numero_colegiado VARCHAR(50) UNIQUE,
    horario_inicio TIME COMMENT 'Hora de inicio de jornada',
    horario_fin TIME COMMENT 'Hora de fin de jornada',
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (especialidad_id) REFERENCES Especialidad(id) ON DELETE RESTRICT
);


CREATE TABLE Horario_Medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    medico_id INT NOT NULL,
    dia_semana ENUM('Lunes','Martes','Miércoles','Jueves','Viernes','Sábado') NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (medico_id) REFERENCES Medico(id) ON DELETE CASCADE,
    UNIQUE KEY (medico_id, dia_semana, hora_inicio)
);


CREATE TABLE Paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(200),
    email VARCHAR(100) UNIQUE,
    dni VARCHAR(20) UNIQUE COMMENT 'Documento de identidad',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);


CREATE TABLE Paciente_Telefono (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    tipo ENUM('casa','celular','trabajo','otro') DEFAULT 'celular',
    principal BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id) ON DELETE CASCADE,
    UNIQUE KEY (paciente_id, telefono)
);

-- 10. Tabla de Citas (core del sistema)
CREATE TABLE Cita (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('pendiente','confirmada','completada','cancelada','no_asistio') DEFAULT 'pendiente',
    duracion_minutos INT DEFAULT 30,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    notas TEXT,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES Medico(id) ON DELETE CASCADE,
    UNIQUE KEY (medico_id, fecha_hora) COMMENT 'Evita solapamientos'
);


CREATE TABLE Motivo_Cita (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cita_id INT NOT NULL,
    motivo TEXT NOT NULL,
    FOREIGN KEY (cita_id) REFERENCES Cita(id) ON DELETE CASCADE
);

CREATE TABLE Usuario_Paciente (
    usuario_id INT PRIMARY KEY,
    paciente_id INT UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id) ON DELETE CASCADE
);


CREATE TABLE Usuario_Medico (
    usuario_id INT PRIMARY KEY,
    medico_id INT UNIQUE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES Medico(id) ON DELETE CASCADE
);

CREATE TABLE Historial_Acciones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    accion VARCHAR(100) NOT NULL COMMENT 'Ej: login, crear_cita',
    tabla_afectada VARCHAR(50),
    registro_id INT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    detalles TEXT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE SET NULL
);

-- 1. Creación de la base de datos
DROP DATABASE IF EXISTS medical;
CREATE DATABASE medical;
USE medical;

-- 2. Tabla de Roles (simplificado a 3 roles)
CREATE TABLE Rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Permisos
CREATE TABLE Permiso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    modulo VARCHAR(50) COMMENT 'Módulo del sistema al que pertenece'
);

-- 4. Relación Rol-Permiso
CREATE TABLE Rol_Permiso (
    rol_id INT,
    permiso_id INT,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES Permiso(id) ON DELETE CASCADE
);

-- 5. Tabla de Usuarios (autenticación centralizada)
CREATE TABLE Usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_login DATETIME,
    FOREIGN KEY (rol_id) REFERENCES Rol(id)
);

-- 6. Tabla de Especialidades Médicas
CREATE TABLE Especialidad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT,
    activa BOOLEAN DEFAULT TRUE
);

-- 7. Tabla de Médicos
CREATE TABLE Medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    especialidad_id INT NOT NULL,
    numero_colegiado VARCHAR(50) UNIQUE NOT NULL,
    horario_inicio TIME DEFAULT '08:00:00',
    horario_fin TIME DEFAULT '17:00:00',
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE SET NULL,
    FOREIGN KEY (especialidad_id) REFERENCES Especialidad(id)
);

-- 8. Tabla de Horarios Médicos
CREATE TABLE Horario_Medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    medico_id INT NOT NULL,
    dia_semana TINYINT NOT NULL COMMENT '1=Lunes, 2=Martes,...,6=Sábado',
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (medico_id) REFERENCES Medico(id) ON DELETE CASCADE,
    CONSTRAINT chk_dia_semana CHECK (dia_semana BETWEEN 1 AND 6),
    UNIQUE KEY (medico_id, dia_semana, hora_inicio)
);

-- 9. Tabla de Pacientes
CREATE TABLE Paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    genero ENUM('masculino', 'femenino', 'otro') NOT NULL,
    direccion TEXT,
    telefono_principal VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    dni VARCHAR(20) UNIQUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE SET NULL
);

-- 10. Tabla de Citas (núcleo del sistema)
CREATE TABLE Cita (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado ENUM('solicitada', 'confirmada', 'completada', 'cancelada', 'no_asistio') DEFAULT 'solicitada',
    duracion INT DEFAULT 30 COMMENT 'Duración en minutos',
    motivo TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    creado_por INT COMMENT 'Usuario que creó la cita',
    notas TEXT,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id),
    FOREIGN KEY (medico_id) REFERENCES Medico(id),
    FOREIGN KEY (creado_por) REFERENCES Usuario(id),
    UNIQUE KEY (medico_id, fecha_hora)
);

-- 11. Tabla de Historial Médico
CREATE TABLE Historial_Medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('consulta', 'diagnostico', 'tratamiento', 'procedimiento', 'nota') NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    observaciones TEXT,
    FOREIGN KEY (paciente_id) REFERENCES Paciente(id),
    FOREIGN KEY (medico_id) REFERENCES Medico(id)
);

-- 12. Tabla de Registro de Actividades
CREATE TABLE Log_Actividad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    accion VARCHAR(100) NOT NULL,
    tabla_afectada VARCHAR(50),
    registro_id INT,
    fecha_hora DATETIME DEFAULT CURRENT_TIMESTAMP,
    detalles JSON,
    ip_address VARCHAR(45),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

-- 13. Insertar datos básicos
INSERT INTO Rol (nombre, descripcion) VALUES 
('Administrador', 'Acceso completo al sistema'),
('Médico', 'Puede gestionar citas, pacientes y historial médico'),
('Paciente', 'Puede solicitar citas y ver su información');

INSERT INTO Permiso (nombre, descripcion, modulo) VALUES 
-- Permisos de administración
('gestionar_usuarios', 'Crear, editar y eliminar usuarios', 'administracion'),
('gestionar_roles', 'Administrar roles y permisos', 'administracion'),
('gestionar_especialidades', 'Administrar especialidades médicas', 'administracion'),

-- Permisos médicos
('ver_citas', 'Ver listado de citas', 'citas'),
('confirmar_citas', 'Confirmar citas solicitadas', 'citas'),
('cancelar_citas', 'Cancelar citas', 'citas'),
('gestionar_historial', 'Editar historial médico', 'historial'),

-- Permisos pacientes
('solicitar_citas', 'Solicitar nuevas citas', 'citas'),
('ver_historial_propio', 'Ver su propio historial médico', 'historial');

-- Asignar permisos a roles
INSERT INTO Rol_Permiso (rol_id, permiso_id) VALUES 
-- Administrador
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7),
-- Médico
(2, 4), (2, 5), (2, 6), (2, 7),
-- Paciente
(3, 8), (3, 9);

-- Insertar especialidades básicas
INSERT INTO Especialidad (nombre, descripcion) VALUES 
('Medicina General', 'Atención primaria y diagnóstico general'),
('Cardiología', 'Especialidad en enfermedades del corazón'),
('Pediatría', 'Medicina para niños y adolescentes');

```