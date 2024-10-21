package com.cl3.alumnosbarrera.serviceImplement;

import com.cl3.alumnosbarrera.model.Alumno;
import com.cl3.alumnosbarrera.model.Usuario;
import com.cl3.alumnosbarrera.repository.AlumnoRepository;
import com.cl3.alumnosbarrera.repository.UsuarioRepository;
import com.cl3.alumnosbarrera.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlumnoServiceImplement implements AlumnoService {

    @Autowired
    private AlumnoRepository dao;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public ResponseEntity<Map<String, Object>> listarAlumnos() {
        Map<String, Object> respuesta = new HashMap<>();
        List<Alumno> alumnos = dao.findAll();

        //Obtener nombreAlumno
        List<Map<String, Object>> alumnosProcesados = new ArrayList<>();

        for (Alumno alumno : alumnos) {
            Map<String, Object> alumnoMap = new HashMap<>();
            alumnoMap.put("id", alumno.getId());
            alumnoMap.put("nombre", alumno.getNombre());
            alumnoMap.put("apellido", alumno.getApellido());
            alumnoMap.put("dni", alumno.getDni());
            alumnoMap.put("ciclo", alumno.getCiclo());
            alumnoMap.put("estado", alumno.getEstado());
            alumnoMap.put("nombre_usuario", alumno.getUsuario().getNombre()); // Solo el nombre
            alumnoMap.put("fecha", alumno.getFecha());

            alumnosProcesados.add(alumnoMap);
        }

        if(!alumnos.isEmpty()){
            respuesta.put("mensaje", "Lista de alumnos");
            respuesta.put("alumnos", alumnosProcesados);
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        } else {
            respuesta.put("mensaje", "No hay registros");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> listarAlumnoPorId(Long id) {
        Map<String, Object> respuesta = new HashMap<>();
        Optional<Alumno> alumnoOpt = dao.findById(id);

        if (alumnoOpt.isPresent()) {
            Alumno alumno = alumnoOpt.get();  // Obtiene el alumno del Optional si está presente

            Map<String, Object> alumnoMap = new HashMap<>();
            alumnoMap.put("id", alumno.getId());
            alumnoMap.put("nombre", alumno.getNombre());
            alumnoMap.put("apellido", alumno.getApellido());
            alumnoMap.put("dni", alumno.getDni());
            alumnoMap.put("ciclo", alumno.getCiclo());
            alumnoMap.put("estado", alumno.getEstado());
            alumnoMap.put("nombre_usuario", alumno.getUsuario().getNombre());  // Obtengo el nombre
            alumnoMap.put("fecha", alumno.getFecha());

            // Armar la respuesta
            respuesta.put("alumno", alumnoMap);
            respuesta.put("mensaje", "Búsqueda correcta");
            respuesta.put("status", HttpStatus.OK);
            respuesta.put("fecha", new Date());
            return ResponseEntity.ok().body(respuesta);

        } else {
            respuesta.put("mensaje", "No hay registros");
            respuesta.put("status", HttpStatus.NOT_FOUND);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> agregarAlumno(Alumno alumno) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            // Validar ciclo
            if (alumno.getCiclo() < 1 || alumno.getCiclo() > 6) {
                respuesta.put("mensaje", "El ciclo debe estar entre 1 y 6");
                respuesta.put("status", HttpStatus.BAD_REQUEST);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // Validar estado
            if (!alumno.getEstado().equals("A") && !alumno.getEstado().equals("I")) {
                respuesta.put("mensaje", "El estado solo puede ser A o I");
                respuesta.put("status", HttpStatus.BAD_REQUEST);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // Obtener el usuario autenticado
            String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
            Usuario usuarioExistente = usuarioRepository.findByEmail(emailAutenticado);
            if (usuarioExistente == null) {
                respuesta.put("mensaje", "Usuario autenticado no encontrado");
                respuesta.put("status", HttpStatus.BAD_REQUEST);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // Asignar el usuario autenticado al alumno
            alumno.setUsuario(usuarioExistente);
            alumno.setFecha(new Date());  // Actualizar fecha
            Alumno nuevoAlumno = dao.save(alumno);

            respuesta.put("alumno", nuevoAlumno);
            respuesta.put("mensaje", "Alumno agregado correctamente");
            respuesta.put("status", HttpStatus.CREATED);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);

        } catch (Exception e) {
            respuesta.put("mensaje", "Error al agregar el alumno");
            respuesta.put("error", e.getMessage());
            respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }


    @Override
    public ResponseEntity<Map<String, Object>> editarAlumno(Alumno alumno, Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            Optional<Alumno> alumnoExistenteOpt = dao.findById(id);

            if (alumnoExistenteOpt.isPresent()) {
                Alumno alumnoExistente = alumnoExistenteOpt.get();

                // Validar ciclo
                if (alumno.getCiclo() < 1 || alumno.getCiclo() > 6) {
                    respuesta.put("mensaje", "El ciclo debe estar entre 1 y 6");
                    respuesta.put("status", HttpStatus.BAD_REQUEST);
                    respuesta.put("fecha", new Date());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Validar estado
                if (!alumno.getEstado().equals("A") && !alumno.getEstado().equals("I")) {
                    respuesta.put("mensaje", "El estado solo puede ser A o I");
                    respuesta.put("status", HttpStatus.BAD_REQUEST);
                    respuesta.put("fecha", new Date());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Obtener el usuario autenticado
                String emailAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
                Usuario usuarioExistente = usuarioRepository.findByEmail(emailAutenticado);
                if (usuarioExistente == null) {
                    respuesta.put("mensaje", "Usuario autenticado no encontrado");
                    respuesta.put("status", HttpStatus.BAD_REQUEST);
                    respuesta.put("fecha", new Date());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Actualizar los datos del alumno existente
                alumnoExistente.setNombre(alumno.getNombre());
                alumnoExistente.setApellido(alumno.getApellido());
                alumnoExistente.setDni(alumno.getDni());
                alumnoExistente.setCiclo(alumno.getCiclo());
                alumnoExistente.setEstado(alumno.getEstado());
                alumnoExistente.setUsuario(usuarioExistente);  // Asignar usuario autenticado
                alumnoExistente.setFecha(new Date());  // Actualizar fecha
                dao.save(alumnoExistente);

                respuesta.put("mensaje", "Alumno actualizado correctamente");
                respuesta.put("alumno", alumnoExistente);
                respuesta.put("status", HttpStatus.OK);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.OK).body(respuesta);
            } else {
                respuesta.put("mensaje", "Alumno no encontrado");
                respuesta.put("status", HttpStatus.NOT_FOUND);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }

        } catch (Exception e) {
            respuesta.put("mensaje", "Error al actualizar el alumno");
            respuesta.put("error", e.getMessage());
            respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }


    @Override
    public ResponseEntity<Map<String, Object>> eliminarAlumno(Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        try {
            Optional<Alumno> alumnoOpt = dao.findById(id);

            if (alumnoOpt.isPresent()) {
                dao.deleteById(id);
                respuesta.put("mensaje", "Alumno eliminado correctamente");
                respuesta.put("status", HttpStatus.OK);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.OK).body(respuesta);
            } else {
                respuesta.put("mensaje", "Alumno no encontrado");
                respuesta.put("status", HttpStatus.NOT_FOUND);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
            }

        } catch (Exception e) {
            respuesta.put("mensaje", "Error al eliminar el alumno");
            respuesta.put("error", e.getMessage());
            respuesta.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            respuesta.put("fecha", new Date());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
        }
    }
}
