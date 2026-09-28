package com.utp.salonbarberiavargas.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utp.salonbarberiavargas.domain.servicios.Servicio;

@Controller
public class ServicioController {
    private final ArrayList<Servicio> servicios = new ArrayList<>();
    private int siguienteId = 5;

    public ServicioController() {
        servicios.add(new Servicio(1, "Corte Clásico & Fade", "Asesoría de imagen, degradado, lavado capilar y peinado con cera mate.", 40, 25.00));
        servicios.add(new Servicio(2, "Perfilado de Barba", "Toalla caliente, perfilado a navaja e hidratación con aceites.", 30, 20.00));
        servicios.add(new Servicio(3, "Combo Vargas Deluxe", "Corte, perfilado de barba y exfoliación facial con vaporizador.", 60, 40.00));
        servicios.add(new Servicio(4, "Tinte y Camuflaje de Canas", "Aplicación de tinte y acabado natural para disimular canas.", 45, 35.00));
    }

    @GetMapping("/servicios")
    public String mostrarServicios(Model model) {
        model.addAttribute("servicios", servicios);
        model.addAttribute("nuevoServicio", new Servicio());
        return "servicios/servicios";
    }

    @PostMapping("/servicios/agregar")
    public String agregarServicio(@ModelAttribute("nuevoServicio") Servicio nuevoServicio) {
        nuevoServicio.setId_servicio(siguienteId++);
        servicios.add(nuevoServicio);
        return "redirect:/servicios";
    }

    @PostMapping("/servicios/editar")
    public String editarServicio(@ModelAttribute("nuevoServicio") Servicio servicioEditado) {
        for (Servicio servicio : servicios) {
            if (servicio.getId_servicio() == servicioEditado.getId_servicio()) {
                servicio.setNombre_servicio(servicioEditado.getNombre_servicio());
                servicio.setDescripcion(servicioEditado.getDescripcion());
                servicio.setDuracion_estimada(servicioEditado.getDuracion_estimada());
                servicio.setPrecio(servicioEditado.getPrecio());
                break;
            }
        }
        return "redirect:/servicios";
    }

    @PostMapping("/servicios/reservar")
    public String reservarServicio(@RequestParam("id_servicio") int idServicio,
            @RequestParam("clienteNombre") String clienteNombre,
            @RequestParam("clienteTelefono") String clienteTelefono,
            @RequestParam("fechaCita") String fechaCita,
            RedirectAttributes redirectAttributes) {
        for (Servicio servicio : servicios) {
            if (servicio.getId_servicio() == idServicio) {
                redirectAttributes.addFlashAttribute("mensajeReserva",
                        "Solicitud recibida para " + servicio.getNombre_servicio() + " a nombre de " + clienteNombre
                                + " (" + clienteTelefono + ") el " + fechaCita + ".");
                return "redirect:/servicios";
            }
        }

        redirectAttributes.addFlashAttribute("mensajeReserva", "No se encontró el servicio seleccionado.");
        return "redirect:/servicios";
    }
}