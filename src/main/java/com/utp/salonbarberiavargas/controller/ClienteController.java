package com.utp.salonbarberiavargas.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.utp.salonbarberiavargas.model.Cliente;
import com.utp.salonbarberiavargas.service.ClienteService;

@Controller
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        model.addAttribute("listaClientes", clienteService.listar());
        return "clientes/clientes";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(@ModelAttribute Cliente nuevoCliente) {
        if (nuevoCliente.getUltimaVisita() == null || nuevoCliente.getUltimaVisita().isBlank()) {
            nuevoCliente.setUltimaVisita("Hoy");
        }
        clienteService.guardar(nuevoCliente);
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/actualizar")
    public String actualizarCliente(@ModelAttribute Cliente clienteActualizado) {
        clienteService.guardar(clienteActualizado);
        return "redirect:/clientes";
    }

    @PostMapping("/clientes/eliminar")
    public String eliminarCliente(@RequestParam("id") Integer id) {
        clienteService.eliminar(id);
        return "redirect:/clientes";
    }
}