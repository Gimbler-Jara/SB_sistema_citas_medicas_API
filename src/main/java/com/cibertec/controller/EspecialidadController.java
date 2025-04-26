package com.cibertec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.Especialidad;
import com.cibertec.service.EspecialidadService;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadController { 

    @Autowired
    private EspecialidadService especialidadService;

    // LISTAR TODAS
    @GetMapping
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        return especialidadService.listarEspecialidades();
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Especialidad>> obtenerPorId(@PathVariable Integer id) {
        return especialidadService.obtenerPorId(id);
    }

    // AGREGAR NUEVA
    @PostMapping
    public ResponseEntity<Especialidad> agregar(@RequestBody Especialidad especialidad) {
        return especialidadService.agregarEspecialidad(especialidad);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Especialidad> actualizar(
            @PathVariable Integer id,
            @RequestBody Especialidad especialidad
    ) {
        return especialidadService.actualizarEspecialidad(id, especialidad);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        return especialidadService.eliminarEspecialidad(id);
    }
}

