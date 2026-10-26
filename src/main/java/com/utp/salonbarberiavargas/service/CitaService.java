package com.utp.salonbarberiavargas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.salonbarberiavargas.model.Cita;
import com.utp.salonbarberiavargas.repository.CitaRepository;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public Cita buscarPorId(Integer id) {
        return citaRepository.findById(id).orElse(null);
    }

    public Cita guardar(Cita cita) {
        return citaRepository.save(cita);
    }

    public void eliminar(Integer id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> buscarPorFecha(String fecha) {
        return citaRepository.findByFecha(fecha);
    }
}
