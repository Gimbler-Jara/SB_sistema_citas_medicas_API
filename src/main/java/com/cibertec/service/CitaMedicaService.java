package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.CitaMedica;
import com.cibertec.repository.CitaMedicaRepository;

@Service
public class CitaMedicaService {

    @Autowired
    private CitaMedicaRepository citaMedicaRepository;

    // LISTAR TODAS LAS CITAS
    public ResponseEntity<List<CitaMedica>> listarCitas() {
        List<CitaMedica> citas = citaMedicaRepository.findAll();
        if (citas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(citas);
        }
    }

    // OBTENER CITA POR ID
    public ResponseEntity<Optional<CitaMedica>> obtenerCitaPorId(Integer id) {
        Optional<CitaMedica> cita = citaMedicaRepository.findById(id);
        if (cita.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(cita); 
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // AGREGAR NUEVA CITA
    public ResponseEntity<CitaMedica> agregarCita(CitaMedica citaMedica) {
        try {
            citaMedicaRepository.save(citaMedica);
            return ResponseEntity.status(HttpStatus.CREATED).body(citaMedica);
        } catch (Exception e) {
            System.out.println("Error al agregar cita médica: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ACTUALIZAR CITA EXISTENTE
    public ResponseEntity<CitaMedica> actualizarCita(Integer id, CitaMedica citaMedica) {
        Optional<CitaMedica> citaExistente = citaMedicaRepository.findById(id);

        if (citaExistente.isPresent()) {
            CitaMedica cita = citaExistente.get();
            cita.setMedico(citaMedica.getMedico());
            cita.setPaciente(citaMedica.getPaciente());
            cita.setFecha(citaMedica.getFecha());
            cita.setHora(citaMedica.getHora());
            cita.setEstado(citaMedica.getEstado());

            try {
                citaMedicaRepository.save(cita);
                return ResponseEntity.status(HttpStatus.CREATED).body(cita);
            } catch (Exception e) {
                System.out.println("Error al actualizar cita médica: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ELIMINAR CITA MÉDICA
    public ResponseEntity<?> eliminarCita(Integer id) {
        Optional<CitaMedica> cita = citaMedicaRepository.findById(id);
        if (cita.isPresent()) {
            try {
                citaMedicaRepository.delete(cita.get());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (Exception e) {
                System.out.println("Error al eliminar cita médica: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
