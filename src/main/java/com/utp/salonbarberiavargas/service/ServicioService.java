package com.utp.salonbarberiavargas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.repository.ServicioRepository;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> listar() {
        return servicioRepository.findAll();
    }

    public Servicio buscarPorId(Integer id) {
        return servicioRepository.findById(id).orElse(null);
    }

    public Servicio guardar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public void eliminar(Integer id) {
        servicioRepository.deleteById(id);
    }
}
