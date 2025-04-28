package com.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.LoginDTO;
import com.cibertec.dto.RegistroMedicoDTO;
import com.cibertec.dto.RegistroPacienteDTO;
import com.cibertec.model.Medico;
import com.cibertec.model.Paciente;
import com.cibertec.model.Usuario;
import com.cibertec.service.MedicoService;
import com.cibertec.service.PacienteService;
import com.cibertec.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private MedicoService medicoService;

	@Autowired
	private PacienteService pacienteService;

	@PostMapping("/medico")
	public ResponseEntity<Medico> registrarMedico(@RequestBody RegistroMedicoDTO dto) {
		Medico medico = medicoService.registrarMedico(dto);
		return ResponseEntity.ok(medico);
	}

	@PostMapping("/paciente")
	public ResponseEntity<Paciente> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
		Paciente paciente = pacienteService.registrarPaciente(dto);
		return ResponseEntity.ok(paciente);
	}

	@PostMapping("login")
	public ResponseEntity<Usuario> login(@RequestBody LoginDTO user) {
		return usuarioService.buscarPorEmail(user);
	}
}