package com.cl3.alumnosbarrera.repository;

import com.cl3.alumnosbarrera.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
}
