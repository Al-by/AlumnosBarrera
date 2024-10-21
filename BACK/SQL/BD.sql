DROP DATABASE APPCIBERTEC2024;

CREATE DATABASE APPCIBERTEC2024;
USE APPCIBERTEC2024;

-- Crear tabla USUARIO con email
CREATE TABLE USUARIO (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50),
  apellido VARCHAR(50),
  email VARCHAR(50) UNIQUE, -- Hacer que el email sea único
  password VARCHAR(255),
  fecha DATE
);

-- Crear tabla ALUMNO
CREATE TABLE ALUMNO (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(50),
  apellido VARCHAR(50),
  dni VARCHAR(8),
  ciclo INT CHECK (ciclo BETWEEN 1 AND 6),
  estado VARCHAR(1) CHECK (estado IN ('A', 'I')),
  nombre_usuario VARCHAR(100),
  fecha DATE,
  FOREIGN KEY (nombre_usuario) REFERENCES USUARIO(email)
);

-- Insertar datos en la tabla USUARIO
INSERT INTO USUARIO (nombre, apellido, email, password, fecha)
VALUES 
('Javier', 'Galarza Calderon', 'javier2024@cibertec.edu.pe', 'cibertec', '2024-10-21'),
('Alvaro', 'Barrera Yulgo', 'alvaro@cibertec.edu.pe', 'alvaro', '2024-10-21');

select * from USUARIO

-- Insertar datos en la tabla ALUMNO
INSERT INTO ALUMNO (nombre, apellido, dni, ciclo, estado, nombre_usuario, fecha)
VALUES 
('Nombre1', 'Apellido1', '12345678', 1, 'A', 'javier2024@cibertec.edu.pe', '2024-10-21'),
('Nombre2', 'Apellido2', '87654321', 2, 'I', 'javier2024@cibertec.edu.pe', '2024-10-21'),
('Nombre3', 'Apellido3', '11223344', 3, 'A', 'alvaro@cibertec.edu.pe', '2024-10-21');

select * from ALUMNO

