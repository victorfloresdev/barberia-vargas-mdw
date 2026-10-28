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
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.service.CategoriaService;
import com.utp.salonbarberiavargas.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Producto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Integer id) {
        Producto producto = productoService.buscarPorId(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@Valid @RequestBody Producto producto, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Integer idCat = null;
        if (producto.getCategoria() != null) {
            idCat = producto.getCategoria().getIdCategoria() != null 
                    ? producto.getCategoria().getIdCategoria() 
                    : producto.getCategoria().getId();
        }

        if (idCat == null) {
            Map<String, List<String>> errores = new HashMap<>();
            errores.put("categoria", List.of("Debe especificar el ID de la categoría"));
            return ResponseEntity.badRequest().body(errores);
        }

        Categoria categoria = categoriaService.buscarPorId(idCat);
        if (categoria == null) {
            Map<String, List<String>> errores = new HashMap<>();
            errores.put("categoria", List.of("La categoría con ID " + idCat + " no existe"));
            return ResponseEntity.badRequest().body(errores);
        }

        producto.setCategoria(categoria);
        Producto guardado = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody Producto datosNuevos, BindingResult resultado) {
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(error -> {
                errores.computeIfAbsent(error.getField(), campo -> new ArrayList<>())
                       .add(error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Producto productoExistente = productoService.buscarPorId(id);
        if (productoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        Integer idCat = null;
        if (datosNuevos.getCategoria() != null) {
            idCat = datosNuevos.getCategoria().getIdCategoria() != null 
                    ? datosNuevos.getCategoria().getIdCategoria() 
                    : datosNuevos.getCategoria().getId();
        }

        if (idCat == null) {
            Map<String, List<String>> errores = new HashMap<>();
            errores.put("categoria", List.of("Debe especificar el ID de la categoría"));
            return ResponseEntity.badRequest().body(errores);
        }

        Categoria categoria = categoriaService.buscarPorId(idCat);
        if (categoria == null) {
            Map<String, List<String>> errores = new HashMap<>();
            errores.put("categoria", List.of("La categoría con ID " + idCat + " no existe"));
            return ResponseEntity.badRequest().body(errores);
        }

        productoExistente.setNombre(datosNuevos.getNombre());
        productoExistente.setPrecio(datosNuevos.getPrecio());
        productoExistente.setStock(datosNuevos.getStock());
        productoExistente.setCategoria(categoria);

        Producto productoActualizado = productoService.guardar(productoExistente);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        Producto productoExistente = productoService.buscarPorId(id);
        if (productoExistente == null) {
            return ResponseEntity.notFound().build();
        }
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
