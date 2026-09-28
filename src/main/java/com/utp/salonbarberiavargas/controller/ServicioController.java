package com.utp.salonbarberiavargas.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utp.salonbarberiavargas.domain.servicios.Servicio;

@Controller
public class ServicioController {
    @GetMapping("/servicios")
    public String mostrarServicios(Model model) {
        ArrayList<Servicio> lista = new ArrayList<>();
        lista.add(new Servicio(1, "Corte Clásico & Fade", "Asesoría de imagen, degradado, lavado capilar y peinado con cera mate.", 40, 25.00));
        lista.add(new Servicio(2, "Perfilado de Barba", "Toalla caliente, perfilado a navaja e hidratación con aceites.", 30, 20.00));
        lista.add(new Servicio(3, "Combo Vargas Deluxe", "Corte, perfilado de barba y exfoliación facial con vaporizador.", 60, 40.00));
        lista.add(new Servicio(4, "Tinte y Camuflaje de Canas", "Aplicación de tinte y acabado natural para disimular canas.", 45, 35.00));

        model.addAttribute("servicios", lista);
        return "servicios/servicios";
    }
}