package com.cibertec.service;

import java.util.List;
import java.util.Optional;

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

	// MÉTODO PARA OBTENER UN DÍA POR ID
	public ResponseEntity<Optional<DiaSemana>> ObtenerDiaPorId(Integer id) {
		Optional<DiaSemana> dia = diaSemanaRepository.findById(id);

		if (dia.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(dia);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// MÉTODO PARA AGREGAR UN NUEVO DÍA
	public ResponseEntity<DiaSemana> AgregarDia(DiaSemana diaSemana) {
		try {
			diaSemanaRepository.save(diaSemana);
			return ResponseEntity.status(HttpStatus.CREATED).body(diaSemana);
		} catch (Exception e) {
			System.out.println("Ocurrió algo inesperado, error: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// MÉTODO PARA ACTUALIZAR UN DÍA EXISTENTE
	public ResponseEntity<DiaSemana> ActualizarDia(Integer id, DiaSemana diaSemana) {
		Optional<DiaSemana> diaExistente = diaSemanaRepository.findById(id);

		if (diaExistente.isPresent()) {
			DiaSemana dia = diaExistente.get();
			dia.setDia(diaSemana.getDia());

			try {
				diaSemanaRepository.save(dia);
				return ResponseEntity.status(HttpStatus.CREATED).body(dia);
			} catch (Exception e) {
				System.out.println("Ocurrió algo inesperado, error: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// MÉTODO PARA ELIMINAR UN DÍA DE LA SEMANA
	public ResponseEntity<?> EliminarDia(Integer id) {
		Optional<DiaSemana> dia = diaSemanaRepository.findById(id);
		if (dia.isPresent()) {
			try {
				diaSemanaRepository.delete(dia.get());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} catch (Exception e) {
				System.out.println("Ocurrió algo inesperado, error: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}
