package com.cl3.alumnosbarrera.serviceImplement;

import com.cl3.alumnosbarrera.model.Alumno;
import com.cl3.alumnosbarrera.model.Usuario;
import com.cl3.alumnosbarrera.repository.AlumnoRepository;
import com.cl3.alumnosbarrera.repository.UsuarioRepository;
import com.cl3.alumnosbarrera.service.AlumnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            // Mensaje de API en caso el ciclo no este entre 1 y 6
            if (alumno.getCiclo() < 1 || alumno.getCiclo() > 6) {
                respuesta.put("mensaje", "El ciclo debe estar entre 1 y 6");
                respuesta.put("status", HttpStatus.BAD_REQUEST);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // Mensaje de API en caso el estado no este entre A o I
            if (!alumno.getEstado().equals("A") && !alumno.getEstado().equals("I")) {
                respuesta.put("mensaje", "El estado solo puede ser A o I");
                respuesta.put("status", HttpStatus.BAD_REQUEST);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }

            // Buscar el Usuario en la base de datos por email (Temporal)
            //TODO CAMBIAR ESTO
            Usuario usuarioExistente = usuarioRepository.findByEmail(alumno.getUsuario().getEmail());
            if (usuarioExistente == null) {
                respuesta.put("mensaje", "Usuario no encontrado");
                respuesta.put("status", HttpStatus.BAD_REQUEST);
                respuesta.put("fecha", new Date());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
            }
            alumno.setUsuario(usuarioExistente);
            alumno.setFecha(new Date());
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
            // Buscar el alumno por id
            Optional<Alumno> alumnoExistenteOpt = dao.findById(id);

            if (alumnoExistenteOpt.isPresent()) {
                Alumno alumnoExistente = alumnoExistenteOpt.get();

                // Mensaje de API en caso el ciclo no este entre 1 y 6
                if (alumno.getCiclo() < 1 || alumno.getCiclo() > 6) {
                    respuesta.put("mensaje", "El ciclo debe estar entre 1 y 6");
                    respuesta.put("status", HttpStatus.BAD_REQUEST);
                    respuesta.put("fecha", new Date());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Mensaje de API en caso el estado no este entre A o I
                if (!alumno.getEstado().equals("A") && !alumno.getEstado().equals("I")) {
                    respuesta.put("mensaje", "El estado solo puede ser A o I");
                    respuesta.put("status", HttpStatus.BAD_REQUEST);
                    respuesta.put("fecha", new Date());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                }

                // Actualizar los campos del alumno existente
                alumnoExistente.setNombre(alumno.getNombre());
                alumnoExistente.setApellido(alumno.getApellido());
                alumnoExistente.setDni(alumno.getDni());
                alumnoExistente.setCiclo(alumno.getCiclo());
                alumnoExistente.setEstado(alumno.getEstado());

                // Buscar el Usuario en la bd si el usuario es diferente
                if (alumno.getUsuario() != null && alumno.getUsuario().getEmail() != null) {
                    Usuario usuarioExistente = usuarioRepository.findByEmail(alumno.getUsuario().getEmail());
                    if (usuarioExistente == null) {
                        respuesta.put("mensaje", "Usuario no encontrado");
                        respuesta.put("status", HttpStatus.BAD_REQUEST);
                        respuesta.put("fecha", new Date());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
                    }
                    alumnoExistente.setUsuario(usuarioExistente); // Asignar el usuario existente al alumno
                }

                alumnoExistente.setFecha(new Date());
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
