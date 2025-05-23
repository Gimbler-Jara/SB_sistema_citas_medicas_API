package com.cibertec.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.cibertec.dto.PacienteActualizacionDTO;
import com.cibertec.dto.RegistroPacienteDTO;

public interface PacienteService {

	ResponseEntity<Map<String, Object>> listarPacientes();

    ResponseEntity<Map<String, Object>> registrarPaciente(RegistroPacienteDTO dto);

    ResponseEntity<Map<String, Object>> actualizarPaciente(Integer id, PacienteActualizacionDTO dto);
}
