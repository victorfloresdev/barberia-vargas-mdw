package com.utp.salonbarberiavargas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.utp.salonbarberiavargas.model.Cita;
import com.utp.salonbarberiavargas.service.CitaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/citas")
public class CitaRestController {

    @Autowired
    private CitaService citaService;

    @GetMapping
    public List<Cita> listar() {
        return citaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> buscarPorId(@PathVariable Integer id) {
        Cita cita = citaService.buscarPorId(id);
        if (cita == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Cita cita, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }
        Cita guardada = citaService.guardar(cita);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody Cita datosNuevos, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Cita citaExistente = citaService.buscarPorId(id);
        if (citaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        datosNuevos.setIdCita(id);
        Cita citaActualizada = citaService.guardar(datosNuevos);
        return ResponseEntity.ok(citaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Cita citaExistente = citaService.buscarPorId(id);
        if (citaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
