package com.cibertec.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.*;
import com.cibertec.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarPacientes() {
        try {
            return pacienteService.listarPacientes();
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("mensaje", "Error al listar pacientes", "httpStatus", 500));
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        try {
            return pacienteService.registrarPaciente(dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Error al registrar paciente", "httpStatus", 500));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarPaciente(
            @PathVariable Integer id,
            @RequestBody PacienteActualizacionDTO dto) {
        try {
            return pacienteService.actualizarPaciente(id, dto);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("mensaje", "Error al actualizar paciente", "httpStatus", 500));
        }
    }
}
