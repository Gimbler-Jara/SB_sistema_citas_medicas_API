package com.cibertec.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_diagnostico")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cita", nullable = false)
    private CitaMedica cita;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;

    @OneToOne(mappedBy = "diagnostico", cascade = CascadeType.ALL)
    private RecetaMedica receta;
}

