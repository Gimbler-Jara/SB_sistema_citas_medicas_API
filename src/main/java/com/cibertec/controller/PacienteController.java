package com.cibertec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.PacienteActualizacionDTO;
import com.cibertec.dto.RegistroPacienteDTO;
import com.cibertec.model.Paciente;
import com.cibertec.service.PacienteService;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        return pacienteService.listarPacientes();
    }
    
    
    @PostMapping
	public ResponseEntity<Paciente> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
		Paciente paciente = pacienteService.registrarPaciente(dto);
		return ResponseEntity.ok(paciente);
	}
    
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPaciente(@PathVariable Integer id, @RequestBody PacienteActualizacionDTO dto) {
        return pacienteService.actualizarPaciente(id, dto);
    }

}
