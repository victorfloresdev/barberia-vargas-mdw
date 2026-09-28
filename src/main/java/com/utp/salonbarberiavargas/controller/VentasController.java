package com.utp.salonbarberiavargas.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.utp.salonbarberiavargas.domain.clientes.Cliente;
import com.utp.salonbarberiavargas.domain.ventas.ItemVenta;

@Controller
public class VentasController {
    private List <ItemVenta> carrito = new ArrayList<>();
    private int contadorId = 1;
    private List<Cliente> clientesSelect = new ArrayList<Cliente>(Arrays.asList(
        new Cliente("#001", "Juan Pérez", "987654321", "15/09/2026"),
        new Cliente("#002", "Carlos Mendoza", "912345678", "20/09/2026")
    ));

    @GetMapping("/ventas")
    public String mostrarVentas(Model model) {
        model.addAttribute("nuevoItem", new ItemVenta());


        model.addAttribute("listaItems", carrito);
        double total = carrito.stream().mapToDouble(ItemVenta::getSubtotal).sum();
        model.addAttribute("totalVenta", total);

        model.addAttribute("listaClientes", clientesSelect);

        return "/ventas/ventas";
    }

    @PostMapping("/ventas/agregar")
    public String agregarItem(@ModelAttribute("nuevoItem") ItemVenta nuevoItem) {
        nuevoItem.setId(contadorId++);
        nuevoItem.setSubtotal(nuevoItem.getCantidad() * nuevoItem.getPrecioUnitario());
        
        carrito.add(nuevoItem);
        

        return "redirect:/ventas";
    }

    @PostMapping("/ventas/eliminar/{id}")
    public String eliminarItem(@PathVariable ("id") int id) {
        carrito.removeIf(item -> item.getId() == id);
        return "redirect:/ventas";
    }

    @PostMapping("/ventas/registrar")
    public String registrarVenta() {
        carrito.clear();
        return "redirect:/ventas";
    }
}
