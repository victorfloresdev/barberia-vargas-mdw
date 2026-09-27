package com.utp.salonbarberiavargas.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
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

    private List<Cita> listaCitas = new ArrayList<>();
    private int contadorId = 110;

    public CitaController() {
        LocalDate hoy = LocalDate.now();
        LocalDate lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        
        listaCitas.add(new Cita("#CT-101", "Juan Pérez", "987654321", "Corte Fade", lunes.toString(), "09:00", 25.0, "Confirmada"));
        listaCitas.add(new Cita("#CT-102", "Marcos Lima", "998877665", "Combo VIP", lunes.plusDays(1).toString(), "11:00", 40.0, "Pendiente"));
        listaCitas.add(new Cita("#CT-103", "Carlos Ruiz", "912345678", "Barba Spa", lunes.plusDays(2).toString(), "09:00", 20.0, "En Curso"));
        listaCitas.add(new Cita("#CT-104", "David Solís", "945612378", "Corte Clásico", lunes.plusDays(3).toString(), "11:00", 25.0, "Confirmada"));
        listaCitas.add(new Cita("#CT-105", "Luis Ramos", "923456789", "Colorimetría", lunes.plusDays(4).toString(), "15:00", 35.0, "Atendida"));
        listaCitas.add(new Cita("#CT-106", "Renato Paz", "934567890", "Fade Completo", lunes.plusDays(4).toString(), "15:00", 25.0, "Cancelada"));

        listaCitas.add(new Cita("#CT-107", "Roberto Sánchez", "966554433", "Corte Fade", hoy.toString(), "09:00", 25.0, "Confirmada"));

        LocalDate lunesSig = lunes.plusWeeks(1);
        listaCitas.add(new Cita("#CT-108", "Anderson Cruz", "955443322", "Corte Fade", lunesSig.toString(), "09:00", 25.0, "Confirmada"));
        listaCitas.add(new Cita("#CT-109", "Mateo Silva", "944332211", "Barba Spa", lunesSig.plusDays(2).toString(), "11:00", 20.0, "Pendiente"));
    }

    private String obtenerNombreMes(int mes) {
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        if (mes >= 1 && mes <= 12) {
            return meses[mes - 1];
        }
        return "";
    }

    private Cita buscarSlotPorFechaHora(String fechaExacta, String horaTarget) {
        return listaCitas.stream()
            .filter(c -> {
                if (c.getFecha() == null || c.getHora() == null) return false;
                boolean matchFecha = c.getFecha().equalsIgnoreCase(fechaExacta)
                    || c.getFecha().replace("-", "/").contains(fechaExacta.replace("-", "/"));
                
                boolean matchHora = c.getHora().startsWith(horaTarget) 
                    || (horaTarget.equals("15:00") && (c.getHora().contains("03:00") || c.getHora().contains("3:00") || c.getHora().contains("15:00")))
                    || (horaTarget.equals("09:00") && (c.getHora().contains("09:00") || c.getHora().contains("9:00")))
                    || (horaTarget.equals("11:00") && c.getHora().contains("11:00"));

                return matchFecha && matchHora;
            })
            .findFirst()
            .orElse(null);
    }

    @GetMapping("/citas")
    public String mostrarCitas(@RequestParam(name = "offset", defaultValue = "0") int offset, Model model) {
        model.addAttribute("listaCitas", listaCitas);
        model.addAttribute("offset", offset);

        LocalDate hoy = LocalDate.now();
        LocalDate lunesBase = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate lunesSemana = lunesBase.plusWeeks(offset);

        int semanaNum = lunesSemana.get(WeekFields.ISO.weekOfYear());
        model.addAttribute("semanaNumero", semanaNum);

        LocalDate fLunes = lunesSemana;
        LocalDate fMartes = lunesSemana.plusDays(1);
        LocalDate fMiercoles = lunesSemana.plusDays(2);
        LocalDate fJueves = lunesSemana.plusDays(3);
        LocalDate fViernes = lunesSemana.plusDays(4);

        model.addAttribute("diaLunesNum", String.format("%02d", fLunes.getDayOfMonth()));
        model.addAttribute("diaLunesMes", obtenerNombreMes(fLunes.getMonthValue()));
        model.addAttribute("fechaLunes", fLunes.toString());

        model.addAttribute("diaMartesNum", String.format("%02d", fMartes.getDayOfMonth()));
        model.addAttribute("diaMartesMes", obtenerNombreMes(fMartes.getMonthValue()));
        model.addAttribute("fechaMartes", fMartes.toString());

        model.addAttribute("diaMiercolesNum", String.format("%02d", fMiercoles.getDayOfMonth()));
        model.addAttribute("diaMiercolesMes", obtenerNombreMes(fMiercoles.getMonthValue()));
        model.addAttribute("fechaMiercoles", fMiercoles.toString());

        model.addAttribute("diaJuevesNum", String.format("%02d", fJueves.getDayOfMonth()));
        model.addAttribute("diaJuevesMes", obtenerNombreMes(fJueves.getMonthValue()));
        model.addAttribute("fechaJueves", fJueves.toString());

        model.addAttribute("diaViernesNum", String.format("%02d", fViernes.getDayOfMonth()));
        model.addAttribute("diaViernesMes", obtenerNombreMes(fViernes.getMonthValue()));
        model.addAttribute("fechaViernes", fViernes.toString());

        model.addAttribute("citasProgramadas", listaCitas.size());
        
        long atendidas = listaCitas.stream()
            .filter(c -> "Atendida".equalsIgnoreCase(c.getEstadoCita()) || "En Curso".equalsIgnoreCase(c.getEstadoCita()))
            .count();
        model.addAttribute("citasAtendidas", atendidas);

        long pendientes = listaCitas.stream()
            .filter(c -> "Pendiente".equalsIgnoreCase(c.getEstadoCita()))
            .count();
        model.addAttribute("citasPendientes", pendientes);

        model.addAttribute("slotLunes9", buscarSlotPorFechaHora(fLunes.toString(), "09:00"));
        model.addAttribute("slotMartes9", buscarSlotPorFechaHora(fMartes.toString(), "09:00"));
        model.addAttribute("slotMiercoles9", buscarSlotPorFechaHora(fMiercoles.toString(), "09:00"));
        model.addAttribute("slotJueves9", buscarSlotPorFechaHora(fJueves.toString(), "09:00"));
        model.addAttribute("slotViernes9", buscarSlotPorFechaHora(fViernes.toString(), "09:00"));

        model.addAttribute("slotLunes11", buscarSlotPorFechaHora(fLunes.toString(), "11:00"));
        model.addAttribute("slotMartes11", buscarSlotPorFechaHora(fMartes.toString(), "11:00"));
        model.addAttribute("slotMiercoles11", buscarSlotPorFechaHora(fMiercoles.toString(), "11:00"));
        model.addAttribute("slotJueves11", buscarSlotPorFechaHora(fJueves.toString(), "11:00"));
        model.addAttribute("slotViernes11", buscarSlotPorFechaHora(fViernes.toString(), "11:00"));

        model.addAttribute("slotLunes15", buscarSlotPorFechaHora(fLunes.toString(), "15:00"));
        model.addAttribute("slotMartes15", buscarSlotPorFechaHora(fMartes.toString(), "15:00"));
        model.addAttribute("slotMiercoles15", buscarSlotPorFechaHora(fMiercoles.toString(), "15:00"));
        model.addAttribute("slotJueves15", buscarSlotPorFechaHora(fJueves.toString(), "15:00"));
        model.addAttribute("slotViernes15", buscarSlotPorFechaHora(fViernes.toString(), "15:00"));

        return "citas/citas";
    }

    @PostMapping("/citas/agregar")
    public String agregarCita(@ModelAttribute Cita nuevaCita, @RequestParam(name = "offset", defaultValue = "0") int offset) {
        if (nuevaCita.getIdCita() == null || nuevaCita.getIdCita().isBlank()) {
            nuevaCita.setIdCita("#CT-" + (contadorId++));
        }
        if (nuevaCita.getEstadoCita() == null || nuevaCita.getEstadoCita().isBlank()) {
            nuevaCita.setEstadoCita("Confirmada");
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
        return redireccionar(offset);
    }

    @PostMapping("/citas/editar")
    public String editarCita(@ModelAttribute Cita citaEditada, @RequestParam(name = "offset", defaultValue = "0") int offset) {
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
        return redireccionar(offset);
    }

    @PostMapping("/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable("id") String id, @RequestParam(name = "offset", defaultValue = "0") int offset) {
        listaCitas.removeIf(c -> c.getIdCita().equalsIgnoreCase(id) 
                              || c.getIdCita().replace("#", "").equalsIgnoreCase(id.replace("#", "")));
        return redireccionar(offset);
    }

    @PostMapping("/citas/eliminar")
    public String eliminarCitaPost(@RequestParam("idCita") String idCita, @RequestParam(name = "offset", defaultValue = "0") int offset) {
        return eliminarCita(idCita, offset);
    }

    private String redireccionar(int offset) {
        return (offset != 0) ? ("redirect:/citas?offset=" + offset) : "redirect:/citas";
    }
}
