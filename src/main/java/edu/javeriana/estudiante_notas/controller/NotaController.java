package edu.javeriana.estudiante_notas.controller;

import edu.javeriana.estudiante_notas.service.NotaService;
import edu.javeriana.estudiante_notas.service.NotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NotaController {

    @Autowired
    private NotaService notaService;

    @GetMapping("/nota")
    public String getNota() {
        return "Nota obtenida";
    }


}
