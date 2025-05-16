package com.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_medico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

	@Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

	@MapsId
	 @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)   
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;
    
    @Column(name = "url_firma_digital", length = 255)
    private String urlFirmaDigital;
}

