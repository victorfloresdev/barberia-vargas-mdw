package com.utp.salonbarberiavargas.controller;

import com.utp.salonbarberiavargas.model.Cliente;
import com.utp.salonbarberiavargas.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController // Indica que este controlador devuelve JSON, no vistas HTML[cite: 4]
@RequestMapping("/api/clientes") // Define la ruta base para la API REST[cite: 7]
public class ClienteRestController {

    @Autowired
    private ClienteService clienteService;

    // 1. LEER TODOS (GET)[cite: 7]
    @GetMapping
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteService.listar());
    }

    // 2. LEER UNO SOLO (GET por ID)[cite: 7]
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable Integer id) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        }
        return ResponseEntity.notFound().build();
    }

    // 3. CREAR (POST) CON VALIDACIÓN DE ERRORES[cite: 7]
    @PostMapping
    public ResponseEntity<?> guardarCliente(@Valid @RequestBody Cliente cliente, BindingResult resultado) {
        // Estructura exacta pedida en la rúbrica usando computeIfAbsent
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(err -> {
                errores.computeIfAbsent(err.getField(), k -> new ArrayList<>()).add(err.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores); // Retorna HTTP 400 con el JSON de errores[cite: 4, 13]
        }
        
        if (cliente.getUltimaVisita() == null || cliente.getUltimaVisita().isEmpty()) {
            cliente.setUltimaVisita("Hoy");
        }

        Cliente nuevoCliente = clienteService.guardar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente); // Retorna HTTP 201[cite: 4, 13]
    }

    // 4. ACTUALIZAR (PUT) CON VALIDACIÓN Y EXTRACCIÓN DE ID[cite: 7, 12]
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Integer id, @Valid @RequestBody Cliente datosNuevos, BindingResult resultado) {
        // Estructura de validación pedida[cite: 12]
        if (resultado.hasErrors()) {
            Map<String, List<String>> errores = new HashMap<>();
            resultado.getFieldErrors().forEach(err -> {
                errores.computeIfAbsent(err.getField(), k -> new ArrayList<>()).add(err.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errores);
        }

        Cliente clienteExistente = clienteService.buscarPorId(id);
        if (clienteExistente == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualizamos los campos con los datos nuevos[cite: 12]
        clienteExistente.setNombreCompleto(datosNuevos.getNombreCompleto());
        clienteExistente.setTelefono(datosNuevos.getTelefono());
        if (datosNuevos.getUltimaVisita() != null && !datosNuevos.getUltimaVisita().isEmpty()) {
            clienteExistente.setUltimaVisita(datosNuevos.getUltimaVisita());
        }

        Cliente clienteActualizado = clienteService.guardar(clienteExistente); // Guardamos en BD[cite: 12]
        return ResponseEntity.ok(clienteActualizado);
    }

    // 5. ELIMINAR (DELETE)[cite: 7]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Integer id) {
        Cliente clienteExistente = clienteService.buscarPorId(id);
        if (clienteExistente != null) {
            clienteService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}