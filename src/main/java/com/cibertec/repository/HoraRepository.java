package com.cibertec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cibertec.model.Hora;


@Repository
public interface HoraRepository extends JpaRepository<Hora, Integer>{

}
