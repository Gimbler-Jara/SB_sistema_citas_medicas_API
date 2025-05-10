package com.cibertec.controller;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        return especialidadService.listarEspecialidades();
    }
}

