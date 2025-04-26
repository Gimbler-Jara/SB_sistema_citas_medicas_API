package com.cibertec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.CitaMedica;
import com.cibertec.service.CitaMedicaService;

@RestController
@RequestMapping("/api/citas")
public class CitaMedicaController {

    @Autowired
    private CitaMedicaService citaMedicaService;

    // LISTAR TODAS LAS CITAS
    @GetMapping
    public ResponseEntity<List<CitaMedica>> listarCitas() {
        return citaMedicaService.listarCitas();
    }

    // OBTENER CITA POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<CitaMedica>> obtenerCitaPorId(@PathVariable Integer id) {
        return citaMedicaService.obtenerCitaPorId(id);
    }

    // AGREGAR NUEVA CITA
    @PostMapping
    public ResponseEntity<CitaMedica> agregarCita(@RequestBody CitaMedica citaMedica) {
        return citaMedicaService.agregarCita(citaMedica);
    }

    // ACTUALIZAR CITA
    @PutMapping("/{id}")
    public ResponseEntity<CitaMedica> actualizarCita(
            @PathVariable Integer id,
            @RequestBody CitaMedica citaMedica
    ) {
        return citaMedicaService.actualizarCita(id, citaMedica);
    }

    // ELIMINAR CITA
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable Integer id) {
        return citaMedicaService.eliminarCita(id);
    }
}


