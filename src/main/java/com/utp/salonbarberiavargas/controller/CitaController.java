package com.utp.salonbarberiavargas.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.utp.salonbarberiavargas.model.Cita;
import com.utp.salonbarberiavargas.model.Cliente;
import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.CitaService;
import com.utp.salonbarberiavargas.service.ClienteService;
import com.utp.salonbarberiavargas.service.ServicioService;

@Controller
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicioService servicioService;

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

    private Cita buscarSlotPorFechaHora(List<Cita> lista, String fechaExacta, String horaTarget) {
        if (lista == null) return null;
        return lista.stream()
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
        List<Cita> listaCitas = citaService.listar();
        List<Cliente> listaClientes = clienteService.listar();
        List<Servicio> listaServicios = servicioService.listar();

        model.addAttribute("listaCitas", listaCitas);
        model.addAttribute("listaClientes", listaClientes);
        model.addAttribute("listaServicios", listaServicios);
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

        model.addAttribute("slotLunes9", buscarSlotPorFechaHora(listaCitas, fLunes.toString(), "09:00"));
        model.addAttribute("slotMartes9", buscarSlotPorFechaHora(listaCitas, fMartes.toString(), "09:00"));
        model.addAttribute("slotMiercoles9", buscarSlotPorFechaHora(listaCitas, fMiercoles.toString(), "09:00"));
        model.addAttribute("slotJueves9", buscarSlotPorFechaHora(listaCitas, fJueves.toString(), "09:00"));
        model.addAttribute("slotViernes9", buscarSlotPorFechaHora(listaCitas, fViernes.toString(), "09:00"));

        model.addAttribute("slotLunes11", buscarSlotPorFechaHora(listaCitas, fLunes.toString(), "11:00"));
        model.addAttribute("slotMartes11", buscarSlotPorFechaHora(listaCitas, fMartes.toString(), "11:00"));
        model.addAttribute("slotMiercoles11", buscarSlotPorFechaHora(listaCitas, fMiercoles.toString(), "11:00"));
        model.addAttribute("slotJueves11", buscarSlotPorFechaHora(listaCitas, fJueves.toString(), "11:00"));
        model.addAttribute("slotViernes11", buscarSlotPorFechaHora(listaCitas, fViernes.toString(), "11:00"));

        model.addAttribute("slotLunes15", buscarSlotPorFechaHora(listaCitas, fLunes.toString(), "15:00"));
        model.addAttribute("slotMartes15", buscarSlotPorFechaHora(listaCitas, fMartes.toString(), "15:00"));
        model.addAttribute("slotMiercoles15", buscarSlotPorFechaHora(listaCitas, fMiercoles.toString(), "15:00"));
        model.addAttribute("slotJueves15", buscarSlotPorFechaHora(listaCitas, fJueves.toString(), "15:00"));
        model.addAttribute("slotViernes15", buscarSlotPorFechaHora(listaCitas, fViernes.toString(), "15:00"));

        return "citas/citas";
    }

    @PostMapping("/citas/agregar")
    public String agregarCita(@ModelAttribute Cita nuevaCita,
                              @RequestParam(name = "idCliente", required = false) Integer idCliente,
                              @RequestParam(name = "idServicio", required = false) Integer idServicio,
                              @RequestParam(name = "offset", defaultValue = "0") int offset) {

        if (nuevaCita.getCliente() == null && idCliente != null) {
            nuevaCita.setCliente(clienteService.buscarPorId(idCliente));
        }

        if (nuevaCita.getServicio() == null && idServicio != null) {
            Servicio serv = servicioService.buscarPorId(idServicio);
            nuevaCita.setServicio(serv);
            if (serv != null && (nuevaCita.getPrecio() == null || nuevaCita.getPrecio() <= 0)) {
                nuevaCita.setPrecio(serv.getPrecio());
            }
        }

        if (nuevaCita.getPrecio() == null || nuevaCita.getPrecio() <= 0) {
            nuevaCita.setPrecio(25.0);
        }

        if (nuevaCita.getEstadoCita() == null || nuevaCita.getEstadoCita().isBlank()) {
            nuevaCita.setEstadoCita("Confirmada");
        }

        if (nuevaCita.getBarbero() == null || nuevaCita.getBarbero().isBlank()) {
            nuevaCita.setBarbero("Carlos Vargas");
        }

        citaService.guardar(nuevaCita);
        return redireccionar(offset);
    }

    @PostMapping("/citas/editar")
    public String editarCita(@ModelAttribute Cita citaEditada,
                             @RequestParam(name = "idCliente", required = false) Integer idCliente,
                             @RequestParam(name = "idServicio", required = false) Integer idServicio,
                             @RequestParam(name = "offset", defaultValue = "0") int offset) {

        if (citaEditada.getIdCita() != null) {
            Cita existente = citaService.buscarPorId(citaEditada.getIdCita());
            if (existente != null) {
                existente.setFecha(citaEditada.getFecha());
                existente.setHora(citaEditada.getHora());

                if (citaEditada.getEstadoCita() != null && !citaEditada.getEstadoCita().isBlank()) {
                    existente.setEstadoCita(citaEditada.getEstadoCita());
                }

                if (citaEditada.getBarbero() != null && !citaEditada.getBarbero().isBlank()) {
                    existente.setBarbero(citaEditada.getBarbero());
                }

                if (idCliente != null) {
                    existente.setCliente(clienteService.buscarPorId(idCliente));
                } else if (citaEditada.getCliente() != null) {
                    existente.setCliente(citaEditada.getCliente());
                }

                if (idServicio != null) {
                    Servicio serv = servicioService.buscarPorId(idServicio);
                    existente.setServicio(serv);
                    if (serv != null) {
                        existente.setPrecio(serv.getPrecio());
                    }
                } else if (citaEditada.getServicio() != null) {
                    existente.setServicio(citaEditada.getServicio());
                }

                if (citaEditada.getPrecio() != null && citaEditada.getPrecio() > 0) {
                    existente.setPrecio(citaEditada.getPrecio());
                }

                citaService.guardar(existente);
            }
        }

        return redireccionar(offset);
    }

    @PostMapping("/citas/eliminar/{id}")
    public String eliminarCita(@PathVariable("id") Integer id, @RequestParam(name = "offset", defaultValue = "0") int offset) {
        citaService.eliminar(id);
        return redireccionar(offset);
    }

    @PostMapping("/citas/eliminar")
    public String eliminarCitaPost(@RequestParam("idCita") Integer idCita, @RequestParam(name = "offset", defaultValue = "0") int offset) {
        return eliminarCita(idCita, offset);
    }

    private String redireccionar(int offset) {
        return (offset != 0) ? ("redirect:/citas?offset=" + offset) : "redirect:/citas";
    }
}
