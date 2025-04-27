package com.cibertec.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cibertec.dto.LoginDTO;
import com.cibertec.dto.RegistroMedicoDTO;
import com.cibertec.dto.RegistroPacienteDTO;
import com.cibertec.dto.UsuarioRequestDTO;
import com.cibertec.model.Medico;
import com.cibertec.model.Paciente;
import com.cibertec.model.Usuario;
import com.cibertec.service.MedicoService;
import com.cibertec.service.PacienteService;
import com.cibertec.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MedicoService medicoService;
    
    @Autowired
    private PacienteService pacienteService;

    // ENDPOINT PARA LISTAR TODOS LOS USUARIOS
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    // ENDPOINT PARA OBTENER UN USUARIO POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Usuario>> obtenerUsuarioPorId(@PathVariable Integer id) {
        return usuarioService.obtenerUsuarioPorId(id);
    }

    // ENDPOINT PARA AGREGAR UN NUEVO USUARIO
    @PostMapping
    public ResponseEntity<Usuario> agregarUsuario(@RequestBody UsuarioRequestDTO usuario) {
        return usuarioService.agregarUsuario(usuario);
    }
    
    
    @PostMapping("/medico")
    public ResponseEntity<Medico> registrarMedico(@RequestBody RegistroMedicoDTO dto) {
        Medico medico = medicoService.registrarMedico(dto);
        return ResponseEntity.ok(medico);
    }
    
    @PostMapping("/paciente")
    public ResponseEntity<Paciente> registrarPaciente(@RequestBody RegistroPacienteDTO dto) {
        Paciente paciente = pacienteService.registrarPaciente(dto);
        return ResponseEntity.ok(paciente);
    }

    // ENDPOINT PARA ACTUALIZAR UN USUARIO EXISTENTE
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario);
    }

    // ENDPOINT PARA ELIMINAR UN USUARIO
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer id) {
        return usuarioService.eliminarUsuario(id);
    }
    
    @PostMapping("login")
    public ResponseEntity<Usuario> login(@RequestBody LoginDTO user) {
        return usuarioService.buscarPorEmail(user);
    }
}