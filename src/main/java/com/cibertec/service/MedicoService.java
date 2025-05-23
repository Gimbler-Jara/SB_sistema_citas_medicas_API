package com.cibertec.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.cibertec.dto.*;
import com.cibertec.model.Medico;

public interface MedicoService {

    ResponseEntity<Map<String, Object>> listarMedicos();

    ResponseEntity<Map<String, Object>> obtenerMedicoPorId(Integer idUsuario);

    Medico registrarMedico(RegistroMedicoDTO dto, MultipartFile archivoFirmaDigital) throws IOException;

    ResponseEntity<Map<String, Object>> actualizarMedico(Integer id, MedicoActualizacionDTO dto, MultipartFile archivoFirmaDigital) throws IOException;

    ResponseEntity<Map<String, Object>> obtenerEspecialidadPorIdMedico(int idMedico);

    ResponseEntity<Map<String, Object>> listarMedicosPorEspecialidad(int idEspecialidad);

    ResponseEntity<Map<String, Object>> listarDiasDisponiblesPorMedico(int idMedico);

    ResponseEntity<Map<String, Object>> listarHorasDisponibles(int idMedico, LocalDate fecha);

    ResponseEntity<Map<String, Object>> listarHorariosDeTrabajoMedico(Integer idMedico);

    ResponseEntity<Map<String, Object>> obtenerUrlFirmaDigital(String urlStorage);
}
