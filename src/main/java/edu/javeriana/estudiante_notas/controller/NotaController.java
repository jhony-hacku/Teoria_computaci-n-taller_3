package edu.javeriana.estudiante_notas.controller;

import edu.javeriana.estudiante_notas.dto.NotaDTO;
import edu.javeriana.estudiante_notas.service.NotaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notas")
@CrossOrigin(origins = "*")
public class NotaController {

    @Autowired
    private NotaService notaService;

    // GET /api/notas/estudiante/{estudianteId}
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<?> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        try {
            List<NotaDTO> notas = notaService.obtenerNotasPorEstudiante(estudianteId);
            return ResponseEntity.ok(notas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // GET /api/notas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(notaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // POST /api/notas  (el body incluye estudianteId)
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody NotaDTO dto) {
        try {
            if (dto.getEstudianteId() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "El estudianteId es obligatorio"));
            }
            NotaDTO creada = notaService.crear(dto.getEstudianteId(), dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // PUT /api/notas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody NotaDTO dto) {
        try {
            NotaDTO actualizada = notaService.actualizar(id, dto);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // DELETE /api/notas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            notaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
