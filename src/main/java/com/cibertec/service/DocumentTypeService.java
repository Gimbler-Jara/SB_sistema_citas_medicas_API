package com.cibertec.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface DocumentTypeService {
	 public ResponseEntity<Map<String, Object>> listarTiposDocumento();
}
