package com.utp.salonbarberiavargas.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.utp.salonbarberiavargas.model.ItemVenta;
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.model.Venta;
import com.utp.salonbarberiavargas.repository.VentaRepository;
import jakarta.transaction.Transactional;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ProductoService productoService;

    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    public Venta buscarPorId(Integer id) {
        return ventaRepository.findById(id).orElse(null);
    }

    @Transactional
    public Venta guardar(Venta venta) {
        if (venta.getFecha() == null) {
            venta.setFecha(LocalDateTime.now());
        }

        if (venta.getItems() != null && !venta.getItems().isEmpty()) {
            double sumaTotal = 0.0;
            for (ItemVenta item : venta.getItems()) {
                item.setVenta(venta);

                double subtotal = item.getCantidad() * item.getPrecioUnitario();
                item.setSubtotal(subtotal);
                sumaTotal += subtotal;

                if ("PRODUCTO".equalsIgnoreCase(item.getTipoItem()) && item.getProducto() != null) {
                    Producto prodBD = productoService.buscarPorId(item.getProducto().getId());
                    if (prodBD != null) {
                        int nuevoStock = prodBD.getStock() - item.getCantidad();
                        prodBD.setStock(Math.max(0, nuevoStock));
                        productoService.guardar(prodBD);
                    }
                }
            }
            venta.setTotal(sumaTotal);
        } else if (venta.getTotal() == null) {
            venta.setTotal(0.0);
        }

        return ventaRepository.save(venta);
    }

    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }
}
