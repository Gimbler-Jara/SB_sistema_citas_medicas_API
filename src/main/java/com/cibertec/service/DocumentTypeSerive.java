package com.cibertec.service;

import java.util.List;
import java.util.Optional;

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

	    // Buscar por ID
	    public ResponseEntity<Optional<DocumentType>> obtenerTipoDocumentoPorId(Integer id) {
	        Optional<DocumentType> tipo = documentTypeRepository.findById(id);
	        if (tipo.isPresent()) {
	            return ResponseEntity.status(HttpStatus.OK).body(tipo);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }

	    // Crear
	    public ResponseEntity<DocumentType> agregarTipoDocumento(DocumentType documentType) {
	        try {
	            DocumentType creado = documentTypeRepository.save(documentType);
	            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
	        } catch (Exception e) {
	            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }

	    // Actualizar
	    public ResponseEntity<DocumentType> actualizarTipoDocumento(Integer id, DocumentType actualizado) {
	        Optional<DocumentType> tipoExistente = documentTypeRepository.findById(id);
	        if (tipoExistente.isPresent()) {
	            DocumentType tipo = tipoExistente.get();
	            tipo.setDoc(actualizado.getDoc()); 
	            try {
	                documentTypeRepository.save(tipo);
	                return ResponseEntity.status(HttpStatus.CREATED).body(tipo);
	            } catch (Exception e) {
	                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }

	    // Eliminar
	    public ResponseEntity<?> eliminarTipoDocumento(Integer id) {
	        Optional<DocumentType> tipo = documentTypeRepository.findById(id);
	        if (tipo.isPresent()) {
	            try {
	                documentTypeRepository.delete(tipo.get());
	                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	            } catch (Exception e) {
	                System.out.println("Ocurrió un error inesperado: " + e.getMessage());
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }
	    }
}
