package com.cibertec.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_hora")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String hora;
}

