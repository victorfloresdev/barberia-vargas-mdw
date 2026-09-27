package com.utp.salonbarberiavargas.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utp.salonbarberiavargas.domain.citas.Cita;

@Controller
public class CitaController {

    @GetMapping("/citas")
    public String mostrarCitas(Model model) {
        List<Cita> listaCitas = new ArrayList<>();
        listaCitas.add(new Cita("#CT-101", "Juan Pérez", "987654321", "Corte Fade", "31/08/2026", "09:00 AM", 25.0, "Confirmada"));
        listaCitas.add(new Cita("#CT-102", "Carlos Ruiz", "912345678", "Barba Spa", "02/09/2026", "09:00 AM", 20.0, "En Curso"));
        listaCitas.add(new Cita("#CT-103", "Marcos Lima", "998877665", "Combo VIP", "01/09/2026", "11:00 AM", 40.0, "Pendiente"));
        listaCitas.add(new Cita("#CT-104", "David Solís", "945612378", "Corte Clásico", "03/09/2026", "11:00 AM", 25.0, "Confirmada"));
        listaCitas.add(new Cita("#CT-105", "Luis Ramos", "923456789", "Colorimetría", "04/09/2026", "03:00 PM", 35.0, "Atendida"));
        listaCitas.add(new Cita("#CT-106", "Renato Paz", "934567890", "Fade Completo", "04/09/2026", "04:30 PM", 25.0, "Cancelada"));

        model.addAttribute("listaCitas", listaCitas);
        return "citas/citas";
    }
}
