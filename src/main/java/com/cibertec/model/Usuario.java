package com.cibertec.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "document_type")
    private DocumentType documentType;

    private String dni;
    private String lastName;
    private String middleName;
    private String firstName;
    private LocalDate birthDate;
    private String gender;
    private String telefono;
    
    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;
    
    @Column(nullable = false)
    private Boolean activo; 
}
