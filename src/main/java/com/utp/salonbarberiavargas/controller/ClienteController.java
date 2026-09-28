package com.utp.salonbarberiavargas.controller;

import com.utp.salonbarberiavargas.domain.clientes.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ClienteController {

    private static List<Cliente> clientes = new ArrayList<>();

    static {
        clientes.add(new Cliente("#001", "Juan Pérez", "987654321", "15/10/2023"));
        clientes.add(new Cliente("#002", "Carlos Mendoza", "912345678", "20/10/2023"));
        clientes.add(new Cliente("#003", "Luis Ramírez", "999888777", "28/09/2026"));
    }

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clientes);
        return "clientes/clientes";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login/login";
    }

    @PostMapping("/clientes/actualizar")
    public String actualizarCliente(@ModelAttribute Cliente clienteActualizado) {
        for (Cliente c : clientes) {
            if (c.getId().equals(clienteActualizado.getId())) {
                c.setNombreCompleto(clienteActualizado.getNombreCompleto());
                c.setTelefono(clienteActualizado.getTelefono());
                c.setUltimaVisita(clienteActualizado.getUltimaVisita());
                break;
            }
        }
        return "redirect:/clientes";
    }
}
