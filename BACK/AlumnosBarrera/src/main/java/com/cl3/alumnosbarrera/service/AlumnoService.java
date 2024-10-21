package com.cl3.alumnosbarrera.service;

import com.cl3.alumnosbarrera.model.Alumno;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface AlumnoService {

    public ResponseEntity<Map<String, Object>> listarAlumnos();
    public ResponseEntity<Map<String, Object>> listarAlumnoPorId(Long id);
    public ResponseEntity<Map<String, Object>> agregarAlumno(Alumno alumno);
    public ResponseEntity<Map<String, Object>> editarAlumno(Alumno alumno, Long id);
    public ResponseEntity<Map<String, Object>> eliminarAlumno(Long id);
}
