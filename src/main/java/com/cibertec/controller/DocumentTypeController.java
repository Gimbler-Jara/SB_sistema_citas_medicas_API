package com.cibertec.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.model.DocumentType;
import com.cibertec.service.DocumentTypeSerive;

import java.util.List;

@RestController
@RequestMapping("/api/document-types")
public class DocumentTypeController {

	@Autowired
	private DocumentTypeSerive documentTypeService;

	@GetMapping
	public ResponseEntity<List<DocumentType>> listarTiposDocumento() {
		return documentTypeService.listarTiposDocumento();
	}
}
