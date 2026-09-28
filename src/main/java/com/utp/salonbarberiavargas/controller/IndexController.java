package com.utp.salonbarberiavargas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller 
public class IndexController {
    @GetMapping("/")
    public String MostrarVistaIndex(Model model) {
        model.addAttribute("citasHoy", 8);
        model.addAttribute("citasPendientes", 2);

        model.addAttribute("ingresosDia", 245.00);
        model.addAttribute("serviciosRealizados", 5);
        model.addAttribute("productosVendidos", 2);

        model.addAttribute("stockBajo", 3);

        return "index";
    }

    public String mostrarVistaCliente() {
        return "clientes/clientes";
    }
    
}
