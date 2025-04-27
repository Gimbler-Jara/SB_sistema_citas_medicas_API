package com.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.DocumentType;
import com.cibertec.service.DocumentTypeSerive;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

	@Autowired
	private DocumentTypeSerive documentTypeService;

	@GetMapping
	public ResponseEntity<List<DocumentType>> listarTiposDocumento() {
		return documentTypeService.listarTiposDocumento();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Optional<DocumentType>> obtenerTipoDocumentoPorId(@PathVariable Integer id) {
		return documentTypeService.obtenerTipoDocumentoPorId(id);
	}

	@PostMapping
	public ResponseEntity<DocumentType> agregarTipoDocumento(@RequestBody DocumentType documentType) {
		return documentTypeService.agregarTipoDocumento(documentType);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DocumentType> actualizarTipoDocumento(@PathVariable Integer id,
			@RequestBody DocumentType actualizado) {
		return documentTypeService.actualizarTipoDocumento(id, actualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminarTipoDocumento(@PathVariable Integer id) {
		return documentTypeService.eliminarTipoDocumento(id);
	}
}
