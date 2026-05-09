package edu.javeriana.estudiante_notas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador para servir vistas estáticas (index, nota).
@Controller
public class VistaController {

    // Mapea raíz e index a la plantilla "index"
    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    // Mapea /nota a la vista "nota"
    @GetMapping({"/nota", "/nota.html"})
    public String nota() {
        return "nota";
    }
}
