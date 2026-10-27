package com.utp.salonbarberiavargas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.utp.salonbarberiavargas.model.Categoria;
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.service.CategoriaService;
import com.utp.salonbarberiavargas.service.ProductoService;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public String listarInventario(Model model) {
        model.addAttribute("listaProductos", productoService.listar());
        model.addAttribute("listaCategorias", categoriaService.listar());
        return "inventario/inventario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Producto producto,
                                  @RequestParam(value = "idCategoria", required = false) Integer idCategoria) {
        if (idCategoria != null) {
            Categoria categoria = categoriaService.buscarPorId(idCategoria);
            producto.setCategoria(categoria);
        } else if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaService.buscarPorId(producto.getCategoria().getIdCategoria());
            producto.setCategoria(categoria);
        }
        productoService.guardar(producto);
        return "redirect:/inventario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoService.eliminar(id);
        return "redirect:/inventario";
    }

    @PostMapping("/categoria/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.guardar(categoria);
        return "redirect:/inventario";
    }

    @GetMapping("/categoria/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id) {
        categoriaService.eliminar(id);
        return "redirect:/inventario";
    }
}
