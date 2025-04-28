package com.cibertec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.DiaSemana;
import com.cibertec.repository.DiaSemanaRepository;

@Service
public class DiaSemanaService {

	@Autowired
	private DiaSemanaRepository diaSemanaRepository;

	// MÉTODO PARA LISTAR TODOS LOS DÍAS DE LA SEMANA
	public ResponseEntity<List<DiaSemana>> ListarDiasSemana() {
		List<DiaSemana> diasSemana = diaSemanaRepository.findAll();
		if (diasSemana.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(diasSemana);
		}
	}
}
