package com.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_medicamento_receta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoReceta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_receta", nullable = false)
    private RecetaMedica receta;

    @Column(nullable = false)
    private String medicamento;

    @Column(columnDefinition = "TEXT")
    private String indicaciones;
}

