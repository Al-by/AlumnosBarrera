package com.cl3.alumnosbarrera.controller;

import com.cl3.alumnosbarrera.model.Alumno;
import com.cl3.alumnosbarrera.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService service;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listar() {
        return service.listarAlumnos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> listaPorID(@PathVariable Long id){
        return service.listarAlumnoPorId(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregar(@Valid @RequestBody Alumno alumno){
        return service.agregarAlumno(alumno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editar(@RequestBody Alumno alumno,@PathVariable Long id){
        return service.editarAlumno(alumno,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        return service.eliminarAlumno(id);
    }

}
