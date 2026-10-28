package com.utp.salonbarberiavargas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.ServicioService;

import jakarta.validation.Valid;

@Controller
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @GetMapping("/servicios")
    public String mostrarServicios(Model model) {
        model.addAttribute("servicios", servicioService.listar());
        if (!model.containsAttribute("nuevoServicio")) {
            model.addAttribute("nuevoServicio", new Servicio());
        }
        return "servicios/servicios";
    }

    @PostMapping({"/servicios/guardar", "/servicios/agregar"})
    public String guardarServicio(@Valid @ModelAttribute("nuevoServicio") Servicio nuevoServicio,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("servicios", servicioService.listar());
            model.addAttribute("errorModal", "agregar");
            return "servicios/servicios";
        }

        nuevoServicio.setIdServicio(null);
        servicioService.guardar(nuevoServicio);
        redirectAttributes.addFlashAttribute("mensajeExito", "Servicio registrado exitosamente.");
        return "redirect:/servicios";
    }

    @PostMapping("/servicios/editar")
    public String editarServicio(@Valid @ModelAttribute("nuevoServicio") Servicio servicioEditado,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("servicios", servicioService.listar());
            model.addAttribute("errorModal", "editar");
            return "servicios/servicios";
        }

        if (servicioEditado.getIdServicio() == null || servicioService.buscarPorId(servicioEditado.getIdServicio()) == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "El servicio a editar no existe.");
            return "redirect:/servicios";
        }

        servicioService.guardar(servicioEditado);
        redirectAttributes.addFlashAttribute("mensajeExito", "Servicio actualizado exitosamente.");
        return "redirect:/servicios";
    }

    @PostMapping("/servicios/eliminar/{id}")
    public String eliminarServicio(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        if (id != null && servicioService.buscarPorId(id) != null) {
            servicioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensajeExito", "Servicio eliminado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError", "No se encontró el servicio a eliminar.");
        }
        return "redirect:/servicios";
    }

    @PostMapping("/servicios/eliminar")
    public String eliminarServicioPost(@RequestParam("idServicio") Integer idServicio, RedirectAttributes redirectAttributes) {
        return eliminarServicio(idServicio, redirectAttributes);
    }

    @GetMapping("/servicios/eliminar/{id}")
    public String eliminarServicioGet(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        return eliminarServicio(id, redirectAttributes);
    }

    @PostMapping("/servicios/reservar")
    public String reservarServicio(@RequestParam("idServicio") Integer idServicio,
                                   @RequestParam("clienteNombre") String clienteNombre,
                                   @RequestParam("clienteTelefono") String clienteTelefono,
                                   @RequestParam("fechaCita") String fechaCita,
                                   RedirectAttributes redirectAttributes) {
        Servicio servicio = servicioService.buscarPorId(idServicio);
        if (servicio != null) {
            redirectAttributes.addFlashAttribute("mensajeReserva",
                    "Solicitud recibida para " + servicio.getNombreServicio() + " a nombre de " + clienteNombre
                            + " (" + clienteTelefono + ") el " + fechaCita + ".");
            return "redirect:/servicios";
        }

        redirectAttributes.addFlashAttribute("mensajeReserva", "No se encontró el servicio seleccionado.");
        return "redirect:/servicios";
    }
}