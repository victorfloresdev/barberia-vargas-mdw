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

import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.ServicioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicios")
public class ServicioRestController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping
    public List<Servicio> listar() {
        return servicioService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> buscarPorId(@PathVariable Integer id) {
        Servicio servicio = servicioService.buscarPorId(id);
        if (servicio == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(servicio);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Servicio servicio, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        servicio.setIdServicio(null);
        Servicio guardado = servicioService.guardar(servicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody Servicio servicio, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Servicio servicioExistente = servicioService.buscarPorId(id);
        if (servicioExistente == null) {
            return ResponseEntity.notFound().build();
        }

        servicio.setIdServicio(id);
        Servicio actualizado = servicioService.guardar(servicio);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Servicio servicioExistente = servicioService.buscarPorId(id);
        if (servicioExistente == null) {
            return ResponseEntity.notFound().build();
        }
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
