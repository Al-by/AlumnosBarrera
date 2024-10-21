package com.cl3.alumnosbarrera.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "ALUMNO")
public class Alumno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellido;

    @Column(length = 8, nullable = false)
    private String dni;

    // Restricción para ciclo entre 1 y 6
    @Min(1)
    @Max(6)
    @Column(nullable = false)
    private Integer ciclo;

    // Restricción para estado ('A' o 'I')
    @Pattern(regexp = "A|I", message = "El estado debe ser 'A' (Activo) o 'I' (Inactivo)")
    @Column(length = 1, nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "nombre_usuario", referencedColumnName = "email", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Date fecha;
}
