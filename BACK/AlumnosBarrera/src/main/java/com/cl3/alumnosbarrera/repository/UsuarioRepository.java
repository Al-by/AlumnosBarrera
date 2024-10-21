package com.cl3.alumnosbarrera.repository;

import com.cl3.alumnosbarrera.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    //Mensaje en API
    Usuario findByEmail(String email);
    //Autenticacion
    Optional<Usuario> findOneByEmail(String email);
}