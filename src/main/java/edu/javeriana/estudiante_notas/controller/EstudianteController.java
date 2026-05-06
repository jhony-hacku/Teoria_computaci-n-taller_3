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

@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "*")
public class EstudianteController {

    @Autowired
    private EstudianteServicio estudianteServicio;

    // GET /api/estudiantes
    @GetMapping
    public ResponseEntity<List<EstudianteDTO>> obtenerTodos() {
        return ResponseEntity.ok(estudianteServicio.obtenerTodos());
    }

    // GET /api/estudiantes/{id}
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
