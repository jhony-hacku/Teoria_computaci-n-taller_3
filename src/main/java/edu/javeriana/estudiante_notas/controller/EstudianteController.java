package edu.javeriana.estudiante_notas.controller;

import edu.javeriana.estudiante_notas.dto.EstudianteDTO;
import edu.javeriana.estudiante_notas.service.EstudianteServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controlador REST para operaciones CRUD sobre Estudiante y cálculo de nota final.
// Responde en /api/estudiantes y maneja posibles errores retornando códigos HTTP adecuados.
@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "*")
public class EstudianteController {

    @Autowired
    private EstudianteServicio estudianteServicio;

    // GET /api/estudiantes
    // Devuelve la lista completa de estudiantes en formato DTO (200 OK).
    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> obtenerTodos() {
        return ResponseEntity.ok(estudianteServicio.obtenerTodos());
    }

    // GET /api/estudiantes/{id}
    // Busca un estudiante por id; si no existe retorna 404 con mensaje.
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(estudianteServicio.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // POST /api/estudiantes
    // Crea un nuevo estudiante; valida entrada y retorna 201 o 409 si correo ya existe.
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody EstudianteDTO dto) {
        try {
            EstudianteDTO creado = estudianteServicio.crear(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /api/estudiantes/{id}
    // Actualiza los datos de un estudiante existente; valida conflictos de correo.
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody EstudianteDTO dto) {
        try {
            EstudianteDTO actualizado = estudianteServicio.actualizar(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // DELETE /api/estudiantes/{id}
    // Elimina estudiante (y sus notas por cascade). Retorna 204 si fue exitoso.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            estudianteServicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // GET /api/estudiantes/{id}/nota-final
    // Calcula la nota final ponderada del estudiante; retorna 200 con la nota o 404 si no existe.
    @GetMapping("/{id}/nota-final")
    public ResponseEntity<?> calcularNotaFinal(@PathVariable Long id) {
        try {
            double notaFinal = estudianteServicio.calcularNotaFinal(id);
            return ResponseEntity.ok(Map.of("notaFinal", notaFinal));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
