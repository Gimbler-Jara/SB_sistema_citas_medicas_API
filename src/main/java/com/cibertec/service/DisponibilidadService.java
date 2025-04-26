package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.Disponibilidad;
import com.cibertec.repository.DisponibilidadRepository;

@Service
public class DisponibilidadService {

	@Autowired
	private DisponibilidadRepository disponibilidadRepository;

	// LISTAR TODAS LAS DISPONIBILIDADES 
	public ResponseEntity<List<Disponibilidad>> listarDisponibilidades() {
		List<Disponibilidad> lista = disponibilidadRepository.findAll();
		if (lista.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(lista);
		}
	}

	// OBTENER DISPONIBILIDAD POR ID
	public ResponseEntity<Optional<Disponibilidad>> obtenerPorId(Integer id) {
		Optional<Disponibilidad> disponibilidad = disponibilidadRepository.findById(id);
		if (disponibilidad.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(disponibilidad);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// AGREGAR UNA NUEVA DISPONIBILIDAD
	public ResponseEntity<Disponibilidad> agregarDisponibilidad(Disponibilidad disponibilidad) {
		try {
			disponibilidadRepository.save(disponibilidad);
			return ResponseEntity.status(HttpStatus.CREATED).body(disponibilidad);
		} catch (Exception e) {
			System.out.println("Error al agregar disponibilidad: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ACTUALIZAR DISPONIBILIDAD EXISTENTE
	public ResponseEntity<Disponibilidad> actualizarDisponibilidad(Integer id, Disponibilidad disponibilidad) {
		Optional<Disponibilidad> disponibilidadExistente = disponibilidadRepository.findById(id);

		if (disponibilidadExistente.isPresent()) {
			Disponibilidad dispo = disponibilidadExistente.get();
			dispo.setMedico(disponibilidad.getMedico());
			dispo.setDiaSemana(disponibilidad.getDiaSemana());
			dispo.setHora(disponibilidad.getHora());
			dispo.setActivo(disponibilidad.getActivo());

			try {
				disponibilidadRepository.save(dispo);
				return ResponseEntity.status(HttpStatus.CREATED).body(dispo);
			} catch (Exception e) {
				System.out.println("Error al actualizar disponibilidad: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	// ELIMINAR DISPONIBILIDAD
	public ResponseEntity<?> eliminarDisponibilidad(Integer id) {
		Optional<Disponibilidad> disponibilidad = disponibilidadRepository.findById(id);
		if (disponibilidad.isPresent()) {
			try {
				disponibilidadRepository.delete(disponibilidad.get());
				return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
			} catch (Exception e) {
				System.out.println("Error al eliminar disponibilidad: " + e.getMessage());
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

}
