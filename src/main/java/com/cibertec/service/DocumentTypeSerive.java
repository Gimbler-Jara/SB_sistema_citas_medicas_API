package com.cibertec.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cibertec.model.DocumentType;
import com.cibertec.repository.DocumentTypeRepository;

@Service
public class DocumentTypeSerive {

	
	 @Autowired
	    private DocumentTypeRepository documentTypeRepository;

	    // Listar todos
	    public ResponseEntity<List<DocumentType>> listarTiposDocumento() {
	        List<DocumentType> tipos = documentTypeRepository.findAll();
	        if (tipos.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        } else {
	            return ResponseEntity.status(HttpStatus.OK).body(tipos);
	        }
	    }
}
