package com.utp.salonbarberiavargas;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.InventarioController;
import com.utp.salonbarberiavargas.model.Categoria;
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.service.CategoriaService;
import com.utp.salonbarberiavargas.service.ProductoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(InventarioController.class)
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private CategoriaService categoriaService;

    @Test
    void testListarInventario() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto prod = new Producto(1, "Cera Pomada", cat, 35.0, 10);

        when(productoService.listar()).thenReturn(List.of(prod));
        when(categoriaService.listar()).thenReturn(List.of(cat));

        mockMvc.perform(get("/inventario"))
                .andExpect(status().isOk())
                .andExpect(view().name("inventario/inventario"))
                .andExpect(model().attributeExists("listaProductos"))
                .andExpect(model().attributeExists("listaCategorias"));
    }

    @Test
    void testGuardarProducto() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        when(categoriaService.buscarPorId(1)).thenReturn(cat);

        mockMvc.perform(post("/inventario/guardar")
                .param("nombre", "Shampoo Anticaspa")
                .param("idCategoria", "1")
                .param("precio", "28.50")
                .param("stock", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventario"));

        verify(productoService).guardar(any(Producto.class));
    }

    @Test
    void testEliminarProducto() throws Exception {
        mockMvc.perform(get("/inventario/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventario"))
                .andExpect(flash().attributeExists("mensajeExito"));

        verify(productoService).eliminar(1);
    }

    @Test
    void testEliminarProductoConVentasAsociadasMuestraError() throws Exception {
        doThrow(new org.springframework.dao.DataIntegrityViolationException("FK constraint"))
                .when(productoService).eliminar(1);

        mockMvc.perform(get("/inventario/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventario"))
                .andExpect(flash().attributeExists("mensajeError"));
    }

    @Test
    void testGuardarCategoria() throws Exception {
        mockMvc.perform(post("/inventario/categoria/guardar")
                .param("nombre", "Coloración"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventario"));

        verify(categoriaService).guardar(any(Categoria.class));
    }
}
