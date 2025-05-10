package com.cibertec.controller;

import java.util.List;

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

	@GetMapping
	public ResponseEntity<List<DiaSemana>> ListarDiasSemana() {
		return diaSemanaService.ListarDiasSemana();
	}
}
