package com.utp.salonbarberiavargas.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.utp.salonbarberiavargas.model.Cliente;
import com.utp.salonbarberiavargas.model.ItemVenta;
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.model.Venta;
import com.utp.salonbarberiavargas.service.ClienteService;
import com.utp.salonbarberiavargas.service.VentaService;
import com.utp.salonbarberiavargas.service.ProductoService;
import com.utp.salonbarberiavargas.service.ServicioService;

@Controller
public class VentasController {
    // Los siguientes errores desaparecerán cuando estén creados los
    // correspondientes servicios
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private VentaService ventaService;

    private List<ItemVenta> carrito = new ArrayList<>();
    private int contadorTemporalId = 1;
    private Integer clienteSeleccionadoId = null;

    @GetMapping("/ventas")
    public String mostrarVentas(Model model) {
        model.addAttribute("nuevoItem", new ItemVenta());
        model.addAttribute("listaItems", carrito);

        double total = carrito.stream().mapToDouble(ItemVenta::getSubtotal).sum();
        model.addAttribute("totalVenta", total);

        model.addAttribute("listaClientes", clienteService.listar());
        model.addAttribute("listaProductos", productoService.listar());
        model.addAttribute("listaServicios", servicioService.listar());
        model.addAttribute("clienteSeleccionadoId", clienteSeleccionadoId);
        return "/ventas/ventas";
    }

    @PostMapping("/ventas/agregar")
    public String agregarItem(
            @ModelAttribute("nuevoItem") ItemVenta nuevoItem,
            @RequestParam(value = "idClienteSeleccionado", required = false) Integer idCliente,
            @RequestParam(value = "idServicioSeleccionado", required = false) Integer idServicio,
            @RequestParam(value = "idProductoSeleccionado", required = false) Integer idProducto) {

        this.clienteSeleccionadoId = idCliente;

        if ("SERVICIO".equalsIgnoreCase(nuevoItem.getTipoItem()) && idServicio != null) {
            Servicio srv = servicioService.buscarPorId(idServicio);
            if (srv != null) {
                nuevoItem.setServicio(srv);
                nuevoItem.setProducto(null);
                nuevoItem.setDescripcion(srv.getNombre_servicio());
                nuevoItem.setPrecioUnitario(srv.getPrecio());
            }
        } else if ("PRODUCTO".equalsIgnoreCase(nuevoItem.getTipoItem()) && idProducto != null) {
            Producto prod = productoService.buscarPorId(idProducto);
            if (prod != null) {
                nuevoItem.setProducto(prod);
                nuevoItem.setServicio(null);
                nuevoItem.setDescripcion(prod.getNombre());
                nuevoItem.setPrecioUnitario(prod.getPrecio());
            }
        }

        if (nuevoItem.getDescripcion() == null || nuevoItem.getDescripcion().isBlank()) {
            return "redirect:/ventas";
        }

        nuevoItem.setSubtotal(nuevoItem.getCantidad() * nuevoItem.getPrecioUnitario());
        nuevoItem.setId(contadorTemporalId++);
        carrito.add(nuevoItem);

        return "redirect:/ventas";
    }

    @PostMapping("/ventas/eliminar/{id}")
    public String eliminarItem(@PathVariable("id") int id) {
        carrito.removeIf(item -> item.getId() == id);
        return "redirect:/ventas";
    }

    @PostMapping("/ventas/registrar")
    public String registrarVenta(@RequestParam(value = "idCliente", required = false) Integer idCliente) {
        if (carrito.isEmpty()) {
            return "redirect:/ventas";
        }

        Venta nuevaVenta = new Venta();
        if (idCliente != null) {
            Cliente cliente = clienteService.buscarPorId(idCliente);
            nuevaVenta.setCliente(cliente);
        }
        for (ItemVenta item : carrito) {
            item.setId(null);
            nuevaVenta.agregarItem(item);
        }

        ventaService.guardar(nuevaVenta);

        carrito.clear();
        contadorTemporalId = 1;
        clienteSeleccionadoId = null;
        return "redirect:/ventas";
    }
}
