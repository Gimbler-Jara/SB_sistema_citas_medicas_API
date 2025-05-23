package com.cibertec.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.DiaSemana;
import com.cibertec.repository.DiaSemanaRepository;
import com.cibertec.service.DiaSemanaService;

@Service
public class DiaSemanaServiceImpl implements DiaSemanaService {

	@Autowired
	private DiaSemanaRepository diaSemanaRepository;

	@Override
	public ResponseEntity<Map<String, Object>> ListarDiasSemana() {
		Map<String, Object> response = new HashMap<>();
		List<DiaSemana> diasSemana = diaSemanaRepository.findAll();

		if (diasSemana.isEmpty()) {
			response.put("mensaje", "No existen registros para la consulta");
			response.put("httpstatus", HttpStatus.NOT_FOUND.value());

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		} else {
			response.put("mensaje", "Lista de dias de semana");
			response.put("httpstatus", HttpStatus.OK.value());
			response.put("diasSemana", diasSemana);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}
}
