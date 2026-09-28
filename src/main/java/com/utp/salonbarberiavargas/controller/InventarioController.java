package com.utp.salonbarberiavargas.controller;

import com.utp.salonbarberiavargas.domain.inventario.Producto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private static final List<Producto> listaProductos = new ArrayList<>();

    static {
        listaProductos.add(new Producto("PRD-01", "Cera Pomada Fijación Mate", "Capilar", 35.00, 15));
        listaProductos.add(new Producto("PRD-02", "Aceite Hidratante para Barba", "Tratamiento", 42.50, 4));
        listaProductos.add(new Producto("PRD-03", "Tinte Negro Intenso Barber", "Coloración", 25.00, 0));
        listaProductos.add(new Producto("PRD-04", "Shampoo Anticaída Fortificante", "Capilar", 38.00, 8));
        listaProductos.add(new Producto("PRD-05", "Loción Aftershave Refrescante", "Tratamiento", 28.00, 2));
    }

    @GetMapping
    public String listarInventario(Model model) {
        model.addAttribute("listaProductos", listaProductos);
        return "inventario/inventario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        boolean existe = false;

        // Si el código ya existe, actualizamos sus campos (Editar)
        for (int i = 0; i < listaProductos.size(); i++) {
            if (listaProductos.get(i).getId().equalsIgnoreCase(producto.getId())) {
                listaProductos.set(i, producto);
                existe = true;
                break;
            }
        }

        if (!existe) {
            listaProductos.add(producto);
        }

        return "redirect:/inventario";
    }

    // Eliminar producto por ID
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") String id) {
        listaProductos.removeIf(prod -> prod.getId().equalsIgnoreCase(id));
        return "redirect:/inventario";
    }
}