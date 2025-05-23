package com.cibertec.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface DiaSemanaService {
	
	public ResponseEntity<Map<String, Object>> ListarDiasSemana();

}
