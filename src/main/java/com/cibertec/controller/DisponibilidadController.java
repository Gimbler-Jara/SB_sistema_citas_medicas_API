package com.cibertec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.Disponibilidad;
import com.cibertec.service.DisponibilidadService;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

	@Autowired
	private DisponibilidadService disponibilidadService;

	// LISTAR TODAS
	@GetMapping
	public ResponseEntity<List<Disponibilidad>> listarDisponibilidades() {
		return disponibilidadService.listarDisponibilidades();
	}

	// OBTENER POR ID
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Disponibilidad>> obtenerPorId(@PathVariable Integer id) {
		return disponibilidadService.obtenerPorId(id);
	}

	// AGREGAR NUEVA
	@PostMapping
	public ResponseEntity<Disponibilidad> agregar(@RequestBody Disponibilidad disponibilidad) {
		return disponibilidadService.agregarDisponibilidad(disponibilidad);
	}

	// ACTUALIZAR EXISTENTE
	@PutMapping("/{id}")
	public ResponseEntity<Disponibilidad> actualizar(@PathVariable Integer id,
			@RequestBody Disponibilidad disponibilidad) {
		return disponibilidadService.actualizarDisponibilidad(id, disponibilidad);
	}

	// ELIMINAR
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		return disponibilidadService.eliminarDisponibilidad(id);
	}
}
