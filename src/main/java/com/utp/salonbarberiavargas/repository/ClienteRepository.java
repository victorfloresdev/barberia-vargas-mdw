package com.utp.salonbarberiavargas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utp.salonbarberiavargas.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> { // El error que marca se corregirá cuando se modifique el tipo de dato de idCliente en la clase Cliente de String a Integer

}