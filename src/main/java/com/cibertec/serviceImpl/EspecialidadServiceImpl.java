package com.cibertec.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.Especialidad;
import com.cibertec.repository.EspecialidadRepository;
import com.cibertec.service.EspecialidadService;

@Service
public class EspecialidadServiceImpl implements EspecialidadService {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Override
    public ResponseEntity<Map<String, Object>> listarEspecialidades() {
        Map<String, Object> response = new HashMap<>();
        List<Especialidad> lista = especialidadRepository.findAll();

        if (lista.isEmpty()) {
            response.put("mensaje", "No existen especialidades registradas");
            response.put("httpstatus", HttpStatus.NOT_FOUND.value());
            response.put("especialidades", lista);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("mensaje", "Lista de especialidades");
            response.put("httpstatus", HttpStatus.OK.value());
            response.put("especialidades", lista);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}

