package com.utp.salonbarberiavargas.repository;

import com.utp.salonbarberiavargas.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    // Al extender JpaRepository<Cliente, Integer>, ya tienes acceso a findAll(), save(), findById(), etc.
}