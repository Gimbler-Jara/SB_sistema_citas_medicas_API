package com.cibertec.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.service.DiaSemanaService;

@RestController
@RequestMapping("/api/diasSemana")
public class DiaSemanaController {

	@Autowired
	private DiaSemanaService diaSemanaService;

	@GetMapping
	public ResponseEntity<Map<String, Object>> ListarDiasSemana() {
		try {
			return diaSemanaService.ListarDiasSemana();
		} catch (Exception e) {
			System.out.println("Error encontrado: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
