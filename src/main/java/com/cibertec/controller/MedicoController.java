package com.cibertec.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.model.Medico;
import com.cibertec.service.MedicoService;


@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
	
	@Autowired
	private MedicoService medicoService;
	
	
	@GetMapping
    public ResponseEntity<List<Medico>> listarEspecialidades() {
        return medicoService.listarMedicos();
    }

}
