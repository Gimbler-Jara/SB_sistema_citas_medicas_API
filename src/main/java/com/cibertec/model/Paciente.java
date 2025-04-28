package com.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

	@Id
    @Column(name = "id_usuario")
    private Integer idUsuario;


    @MapsId
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}

