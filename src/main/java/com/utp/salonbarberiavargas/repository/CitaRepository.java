package com.utp.salonbarberiavargas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utp.salonbarberiavargas.model.Cita;

public interface CitaRepository extends JpaRepository<Cita, Integer>{ // El error que marca se corregirá cuando se modifique el tipo de dato de idCita en la clase Cita de String a Integer

}
