package com.cibertec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.EstadoCita;
import com.cibertec.service.EstadoCitaService;

@RestController
@RequestMapping("/api/estadosCita")
public class EstadoCitaController {

	@Autowired
	private EstadoCitaService estadoCitaService;

	// LISTAR TODOS LOS ESTADOS
	@GetMapping
	public ResponseEntity<List<EstadoCita>> listarEstados() {
		return estadoCitaService.listarEstados();
	}

	// OBTENER ESTADO POR ID
	@GetMapping("/{id}")
	public ResponseEntity<Optional<EstadoCita>> obtenerPorId(@PathVariable Integer id) {
		return estadoCitaService.obtenerPorId(id);
	}

	// AGREGAR NUEVO ESTADO
	@PostMapping
	public ResponseEntity<EstadoCita> agregar(@RequestBody EstadoCita estadoCita) {
		return estadoCitaService.agregarEstado(estadoCita);
	}

	// ACTUALIZAR ESTADO
	@PutMapping("/{id}")
	public ResponseEntity<EstadoCita> actualizar(@PathVariable Integer id, @RequestBody EstadoCita estadoCita) {
		return estadoCitaService.actualizarEstado(id, estadoCita);
	}

	// ELIMINAR ESTADO
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Integer id) {
		return estadoCitaService.eliminarEstado(id);
	}
}
