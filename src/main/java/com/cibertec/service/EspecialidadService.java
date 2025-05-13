package com.cibertec.service;

import java.util.List;

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
}

