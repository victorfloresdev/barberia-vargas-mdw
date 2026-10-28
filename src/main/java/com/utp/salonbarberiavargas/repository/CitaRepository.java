package com.utp.salonbarberiavargas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.salonbarberiavargas.model.Cita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByFecha(String fecha);

    List<Cita> findByEstadoCita(String estadoCita);
}
