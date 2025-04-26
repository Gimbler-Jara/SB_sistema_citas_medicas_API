package com.cibertec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.DiaSemana;
import com.cibertec.service.DiaSemanaService;

@RestController
@RequestMapping("/api/diasSemana")
public class DiaSemanaController {

	@Autowired
	private DiaSemanaService diaSemanaService;

	// ENDPOINT PARA LISTAR TODOS LOS DÍAS DE LA SEMANA
	@GetMapping
	public ResponseEntity<List<DiaSemana>> ListarDiasSemana() {
		return diaSemanaService.ListarDiasSemana();
	}

	// ENDPOINT PARA OBTENER UN DÍA POR ID
	@GetMapping("/{id}")
	public ResponseEntity<Optional<DiaSemana>> ObtenerDiaPorId(@PathVariable Integer id) {
		return diaSemanaService.ObtenerDiaPorId(id);
	}

	// ENDPOINT PARA AGREGAR UN NUEVO DÍA
	@PostMapping
	public ResponseEntity<DiaSemana> AgregarDia(@RequestBody DiaSemana diaSemana) {
		return diaSemanaService.AgregarDia(diaSemana);
	}

	// ENDPOINT PARA ACTUALIZAR UN DÍA EXISTENTE
	@PutMapping("/{id}")
	public ResponseEntity<DiaSemana> ActualizarDia(@PathVariable Integer id, @RequestBody DiaSemana diaSemana) {
		return diaSemanaService.ActualizarDia(id, diaSemana);
	}

	// ENDPOINT PARA ELIMINAR UN DÍA
	@DeleteMapping("/{id}")
	public ResponseEntity<?> EliminarDia(@PathVariable Integer id) {
		return diaSemanaService.EliminarDia(id);
	}

}
