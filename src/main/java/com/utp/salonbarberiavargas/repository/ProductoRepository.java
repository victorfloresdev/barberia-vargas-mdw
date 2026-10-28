package com.utp.salonbarberiavargas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utp.salonbarberiavargas.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
