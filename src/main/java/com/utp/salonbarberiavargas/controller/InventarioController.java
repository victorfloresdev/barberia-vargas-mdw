package com.utp.salonbarberiavargas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                                  @RequestParam(value = "idCategoria", required = false) Integer idCategoria,
                                  RedirectAttributes flash) {
        if (idCategoria != null) {
            Categoria categoria = categoriaService.buscarPorId(idCategoria);
            producto.setCategoria(categoria);
        } else if (producto.getCategoria() != null && producto.getCategoria().getIdCategoria() != null) {
            Categoria categoria = categoriaService.buscarPorId(producto.getCategoria().getIdCategoria());
            producto.setCategoria(categoria);
        }
        productoService.guardar(producto);
        flash.addFlashAttribute("mensajeExito", "Producto guardado correctamente.");
        return "redirect:/inventario";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id, RedirectAttributes flash) {
        try {
            productoService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "Producto eliminado correctamente.");
        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute("mensajeError", "No se puede eliminar el producto porque tiene ventas registradas.");
        }
        return "redirect:/inventario";
    }

    @PostMapping("/categoria/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria, RedirectAttributes flash) {
        categoriaService.guardar(categoria);
        flash.addFlashAttribute("mensajeExito", "Categoría guardada correctamente.");
        return "redirect:/inventario";
    }

    @GetMapping("/categoria/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Integer id, RedirectAttributes flash) {
        try {
            categoriaService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "Categoría eliminada correctamente.");
        } catch (DataIntegrityViolationException ex) {
            flash.addFlashAttribute("mensajeError", "No se puede eliminar la categoría porque tiene productos asociados.");
        }
        return "redirect:/inventario";
    }
}
