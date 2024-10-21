package com.cl3.alumnosbarrera.repository;

import com.cl3.alumnosbarrera.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}
