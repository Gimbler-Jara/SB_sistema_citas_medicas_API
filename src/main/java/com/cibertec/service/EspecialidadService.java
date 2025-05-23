package com.cibertec.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;

public interface EspecialidadService {
    public ResponseEntity<Map<String, Object>> listarEspecialidades();
}
