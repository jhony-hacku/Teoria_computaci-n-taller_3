package edu.javeriana.estudiante_notas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    @GetMapping({"/nota", "/nota.html"})
    public String nota() {
        return "nota";
    }
}
