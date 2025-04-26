package com.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_disponibilidad")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disponibilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idMedico", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "idDiaSemana", nullable = false)
    private DiaSemana diaSemana;

    @ManyToOne
    @JoinColumn(name = "idHora", nullable = false)
    private Hora hora;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    private Boolean activo;
}

