package com.utp.salonbarberiavargas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.utp.salonbarberiavargas.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{ // El error que marca se corregirá cuando se modifique el tipo de dato de id en la clase Producto de String a Integer

}
