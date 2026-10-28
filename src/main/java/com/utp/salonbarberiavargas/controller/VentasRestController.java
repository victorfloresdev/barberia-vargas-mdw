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

import com.utp.salonbarberiavargas.model.Venta;
import com.utp.salonbarberiavargas.service.VentaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ventas")
public class VentasRestController {
    @Autowired
    private VentaService ventaService;

    @GetMapping
    public List<Venta> listar() {
        return ventaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Integer id) {
        Venta venta = ventaService.buscarPorId(id);
        if (venta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venta);
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Venta venta, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), key -> new ArrayList<>())
                        .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }
        Venta nuevaVenta = ventaService.guardar(venta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaVenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody Venta venta,
            BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), key -> new ArrayList<>())
                        .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Venta ventaExistente = ventaService.buscarPorId(id);
        if (ventaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        venta.setIdVenta(id);
        if (venta.getItems() == null || venta.getItems().isEmpty()) {
            venta.setItems(ventaExistente.getItems());
        }

        Venta ventaActualizada = ventaService.guardar(venta);
        return ResponseEntity.ok(ventaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Venta ventaExistente = ventaService.buscarPorId(id);
        if (ventaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
