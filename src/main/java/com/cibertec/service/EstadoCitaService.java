package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.EstadoCita;
import com.cibertec.repository.EstadoCitaRepository;

@Service
public class EstadoCitaService {

	@Autowired
    private EstadoCitaRepository estadoCitaRepository;


    // LISTAR TODOS LOS ESTADOS DE CITA
    public ResponseEntity<List<EstadoCita>> listarEstados() {
        List<EstadoCita> estados = estadoCitaRepository.findAll();
        if (estados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(estados);
        }
    }

    // OBTENER ESTADO POR ID
    public ResponseEntity<Optional<EstadoCita>> obtenerPorId(Integer id) {
        Optional<EstadoCita> estado = estadoCitaRepository.findById(id);
        if (estado.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(estado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // AGREGAR NUEVO ESTADO
    public ResponseEntity<EstadoCita> agregarEstado(EstadoCita estadoCita) {
        try {
            estadoCitaRepository.save(estadoCita);
            return ResponseEntity.status(HttpStatus.CREATED).body(estadoCita);
        } catch (Exception e) {
            System.out.println("Error al agregar estado de cita: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ACTUALIZAR ESTADO EXISTENTE
    public ResponseEntity<EstadoCita> actualizarEstado(Integer id, EstadoCita estadoCita) {
        Optional<EstadoCita> estadoExistente = estadoCitaRepository.findById(id);

        if (estadoExistente.isPresent()) {
            EstadoCita estado = estadoExistente.get();
            estado.setEstado(estadoCita.getEstado());

            try {
                estadoCitaRepository.save(estado);
                return ResponseEntity.status(HttpStatus.CREATED).body(estado);
            } catch (Exception e) {
                System.out.println("Error al actualizar estado de cita: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ELIMINAR ESTADO
    public ResponseEntity<?> eliminarEstado(Integer id) {
        Optional<EstadoCita> estado = estadoCitaRepository.findById(id);
        if (estado.isPresent()) {
            try {
                estadoCitaRepository.delete(estado.get());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (Exception e) {
                System.out.println("Error al eliminar estado de cita: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

