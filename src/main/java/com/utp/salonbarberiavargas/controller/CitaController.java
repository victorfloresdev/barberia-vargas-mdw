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
import org.springframework.web.bind.annotation.RequestParam;

import com.utp.salonbarberiavargas.domain.citas.Cita;

@Controller
public class CitaController {

    private List<Cita> listaCitas = new ArrayList<>(Arrays.asList(
        new Cita("#CT-101", "Juan Pérez", "987654321", "Corte Fade", "2026-08-31", "09:00", 25.0, "Confirmada"),
        new Cita("#CT-102", "Carlos Ruiz", "912345678", "Barba Spa", "2026-09-02", "09:00", 20.0, "En Curso"),
        new Cita("#CT-103", "Marcos Lima", "998877665", "Combo VIP", "2026-09-01", "11:00", 40.0, "Pendiente"),
        new Cita("#CT-104", "David Solís", "945612378", "Corte Clásico", "2026-09-03", "11:00", 25.0, "Confirmada"),
        new Cita("#CT-105", "Luis Ramos", "923456789", "Colorimetría", "2026-09-04", "15:00", 35.0, "Atendida"),
        new Cita("#CT-106", "Renato Paz", "934567890", "Fade Completo", "2026-09-04", "15:00", 25.0, "Cancelada")
    ));

    private int contadorId = 107;

    private Cita buscarSlot(String dia, String horaPrefix) {
        return listaCitas.stream()
            .filter(c -> c.getFecha() != null && c.getFecha().contains(dia) 
                      && c.getHora() != null && (c.getHora().startsWith(horaPrefix) || c.getHora().contains(horaPrefix)))
            .findFirst()
            .orElse(null);
    }

    @GetMapping("/citas")
    public String mostrarCitas(Model model) {
        model.addAttribute("listaCitas", listaCitas);

        // Contadores dinámicos para las tarjetas superiores (cards)
        model.addAttribute("citasProgramadas", listaCitas.size());
        long atendidas = listaCitas.stream()
            .filter(c -> "Atendida".equalsIgnoreCase(c.getEstadoCita()) || "En Curso".equalsIgnoreCase(c.getEstadoCita()))
            .count();
        model.addAttribute("citasAtendidas", atendidas);
        long pendientes = listaCitas.stream()
            .filter(c -> "Pendiente".equalsIgnoreCase(c.getEstadoCita()))
            .count();
        model.addAttribute("citasPendientes", pendientes);

        // Citas para la cuadrícula semanal interactiva (cards de la agenda)
        model.addAttribute("slotLunes9", buscarSlot("31", "09"));
        model.addAttribute("slotMartes9", buscarSlot("01", "09"));
        model.addAttribute("slotMiercoles9", buscarSlot("02", "09"));
        model.addAttribute("slotJueves9", buscarSlot("03", "09"));
        model.addAttribute("slotViernes9", buscarSlot("04", "09"));

        model.addAttribute("slotLunes11", buscarSlot("31", "11"));
        model.addAttribute("slotMartes11", buscarSlot("01", "11"));
        model.addAttribute("slotMiercoles11", buscarSlot("02", "11"));
        model.addAttribute("slotJueves11", buscarSlot("03", "11"));
        model.addAttribute("slotViernes11", buscarSlot("04", "11"));

        model.addAttribute("slotLunes15", buscarSlot("31", "15"));
        model.addAttribute("slotMartes15", buscarSlot("01", "15"));
        model.addAttribute("slotMiercoles15", buscarSlot("02", "15"));
        model.addAttribute("slotJueves15", buscarSlot("03", "15"));
        model.addAttribute("slotViernes15", buscarSlot("04", "15"));

        return "citas/citas";
    }

    @PostMapping("/citas/agregar")
    public String agregarCita(@ModelAttribute Cita nuevaCita) {
        if (nuevaCita.getIdCita() == null || nuevaCita.getIdCita().isBlank()) {
            nuevaCita.setIdCita("#CT-" + (contadorId++));
        }
        if (nuevaCita.getEstadoCita() == null || nuevaCita.getEstadoCita().isBlank()) {
            nuevaCita.setEstadoCita("Pendiente");
        }
        if (nuevaCita.getPrecio() <= 0) {
            if ("Barba Spa".equalsIgnoreCase(nuevaCita.getNombreServicio())) {
                nuevaCita.setPrecio(20.0);
            } else if ("Combo VIP".equalsIgnoreCase(nuevaCita.getNombreServicio())) {
                nuevaCita.setPrecio(40.0);
            } else if ("Colorimetría".equalsIgnoreCase(nuevaCita.getNombreServicio())) {
                nuevaCita.setPrecio(35.0);
            } else {
                nuevaCita.setPrecio(25.0);
            }
        }
        listaCitas.add(nuevaCita);
        return "redirect:/citas";
    }

    @PostMapping("/citas/editar")
    public String editarCita(@ModelAttribute Cita citaEditada) {
        for (Cita c : listaCitas) {
            if (c.getIdCita().equalsIgnoreCase(citaEditada.getIdCita())) {
                c.setNombreCliente(citaEditada.getNombreCliente());
                c.setNombreServicio(citaEditada.getNombreServicio());
                c.setFecha(citaEditada.getFecha());
                c.setHora(citaEditada.getHora());
                if (citaEditada.getEstadoCita() != null && !citaEditada.getEstadoCita().isBlank()) {
                    c.setEstadoCita(citaEditada.getEstadoCita());
                }
                if (citaEditada.getTelefono() != null && !citaEditada.getTelefono().isBlank()) {
                    c.setTelefono(citaEditada.getTelefono());
                }
                if ("Barba Spa".equalsIgnoreCase(citaEditada.getNombreServicio())) {
                    c.setPrecio(20.0);
                } else if ("Combo VIP".equalsIgnoreCase(citaEditada.getNombreServicio())) {
                    c.setPrecio(40.0);
                } else if ("Colorimetría".equalsIgnoreCase(citaEditada.getNombreServicio())) {
                    c.setPrecio(35.0);
                } else {
                    c.setPrecio(25.0);
                }
                break;
            }
        }
        return "redirect:/citas";
    }

    @PostMapping("/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable("id") String id) {
        listaCitas.removeIf(c -> c.getIdCita().equalsIgnoreCase(id) 
                              || c.getIdCita().replace("#", "").equalsIgnoreCase(id.replace("#", "")));
        return "redirect:/citas";
    }

    @PostMapping("/citas/eliminar")
    public String eliminarCitaPost(@RequestParam("idCita") String idCita) {
        return eliminarCita(idCita);
    }
}
