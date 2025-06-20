package com.cibertec.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.DocumentType;
import com.cibertec.repository.DocumentTypeRepository;
import com.cibertec.service.DocumentTypeService;


@Service
public class DocumentTypeSeriveImpl implements DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Override
    public ResponseEntity<Map<String, Object>> listarTiposDocumento() {
        Map<String, Object> response = new HashMap<>();
        List<DocumentType> tipos = documentTypeRepository.findAll();

        if (tipos.isEmpty()) {
            response.put("mensaje", "No existen tipos de documento registrados");
            response.put("httpstatus", HttpStatus.NOT_FOUND.value());
            response.put("documentos", tipos);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("mensaje", "Lista de tipos de documento");
            response.put("httpstatus", HttpStatus.OK.value());
            response.put("documentos", tipos);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }
}
