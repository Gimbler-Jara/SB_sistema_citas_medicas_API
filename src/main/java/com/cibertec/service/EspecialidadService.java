package com.cibertec.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.Especialidad;
import com.cibertec.repository.EspecialidadRepository;

@Service
public class EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    // LISTAR TODAS LAS ESPECIALIDADES
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        List<Especialidad> lista = especialidadRepository.findAll(); 
        if (lista.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        }
    }

    // OBTENER UNA ESPECIALIDAD POR ID
    public ResponseEntity<Optional<Especialidad>> obtenerPorId(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        if (especialidad.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(especialidad);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // AGREGAR UNA NUEVA ESPECIALIDAD
    public ResponseEntity<Especialidad> agregarEspecialidad(Especialidad especialidad) {
        try {
            especialidadRepository.save(especialidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(especialidad);
        } catch (Exception e) {
            System.out.println("Error al agregar especialidad: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ACTUALIZAR UNA ESPECIALIDAD
    public ResponseEntity<Especialidad> actualizarEspecialidad(Integer id, Especialidad especialidad) {
        Optional<Especialidad> especialidadExistente = especialidadRepository.findById(id);

        if (especialidadExistente.isPresent()) {
            Especialidad esp = especialidadExistente.get();
            esp.setEspecialidad(especialidad.getEspecialidad());

            try {
                especialidadRepository.save(esp);
                return ResponseEntity.status(HttpStatus.CREATED).body(esp);
            } catch (Exception e) {
                System.out.println("Error al actualizar especialidad: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // ELIMINAR UNA ESPECIALIDAD
    public ResponseEntity<?> eliminarEspecialidad(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        if (especialidad.isPresent()) {
            try {
                especialidadRepository.delete(especialidad.get());
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } catch (Exception e) {
                System.out.println("Error al eliminar especialidad: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

