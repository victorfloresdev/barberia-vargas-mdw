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

import com.utp.salonbarberiavargas.model.Categoria;
import com.utp.salonbarberiavargas.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Categoria categoria, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }
        Categoria guardada = categoriaService.guardar(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody Categoria datosNuevos, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Categoria categoriaExistente = categoriaService.buscarPorId(id);
        if (categoriaExistente == null) {
            return ResponseEntity.notFound().build();
        }

        datosNuevos.setIdCategoria(id);
        Categoria categoriaActualizada = categoriaService.guardar(datosNuevos);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Categoria categoriaExistente = categoriaService.buscarPorId(id);
        if (categoriaExistente == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
