package com.utp.salonbarberiavargas.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.utp.salonbarberiavargas.model.Cita;
import com.utp.salonbarberiavargas.model.ItemVenta;
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.model.Venta;
import com.utp.salonbarberiavargas.service.CitaService;
import com.utp.salonbarberiavargas.service.VentaService;

@Controller
public class IndexController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping({ "/", "/index" })
    public String MostrarVistaIndex(Model model) {
        String hoyStr = LocalDate.now().toString();

        List<Cita> todasLasCitas = citaService.listar();
        long citasHoy = todasLasCitas.stream()
                .filter(c -> hoyStr.equals(c.getFecha()))
                .count();
        long citasPendientes = todasLasCitas.stream()
                .filter(c -> "Pendiente".equalsIgnoreCase(c.getEstadoCita()))
                .count();
        List<Venta> todasLasVentas = ventaService.listar();
        List<Venta> ventasHoy = todasLasVentas.stream()
                .filter(v -> v.getFecha() != null && v.getFecha().toLocalDate().equals(LocalDate.now()))
                .toList();
        double ingresosDia = ventasHoy.stream()
                .mapToDouble(Venta::getTotal)
                .sum();
        int serviciosRealizados = 0;
        int productosVendidos = 0;
        for (Venta v : ventasHoy) {
            if (v.getItems() != null) {
                for (ItemVenta item : v.getItems()) {
                    if ("SERVICIO".equalsIgnoreCase(item.getTipoItem())) {
                        serviciosRealizados += item.getCantidad();
                    } else if ("PRODUCTO".equalsIgnoreCase(item.getTipoItem())) {
                        productosVendidos += item.getCantidad();
                    }
                }
            }
        }
        List<Producto> todosLosProductos = productoService.listar();
        long stockBajo = todosLosProductos.stream()
                .filter(p -> p.getStock() <= 5)
                .count();

        model.addAttribute("citasHoy", citasHoy);
        model.addAttribute("citasPendientes", citasPendientes);
        model.addAttribute("ingresosDia", String.format("%.2f", ingresosDia));
        model.addAttribute("serviciosRealizados", serviciosRealizados);
        model.addAttribute("productosVendidos", productosVendidos);
        model.addAttribute("stockBajo", stockBajo);

        return "index";
    }

}
