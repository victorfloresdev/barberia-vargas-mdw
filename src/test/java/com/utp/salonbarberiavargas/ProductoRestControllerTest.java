package com.utp.salonbarberiavargas;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.ProductoRestController;
import com.utp.salonbarberiavargas.model.Categoria;
import com.utp.salonbarberiavargas.model.Producto;
import com.utp.salonbarberiavargas.service.CategoriaService;
import com.utp.salonbarberiavargas.service.ProductoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoRestController.class)
class ProductoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private CategoriaService categoriaService;

    @Test
    void testListarProductos() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto prod = new Producto(1, "Cera Fijadora", cat, 35.0, 10);

        when(productoService.listar()).thenReturn(List.of(prod));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idProducto").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Cera Fijadora"))
                .andExpect(jsonPath("$[0].precio").value(35.0))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].categoria.nombre").value("Capilar"));
    }

    @Test
    void testBuscarPorIdExistente() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto prod = new Producto(1, "Cera Fijadora", cat, 35.0, 10);

        when(productoService.buscarPorId(1)).thenReturn(prod);

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1))
                .andExpect(jsonPath("$.nombre").value("Cera Fijadora"));
    }

    @Test
    void testBuscarPorIdNotFound() throws Exception {
        when(productoService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(get("/api/productos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGuardarProductoValido() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto guardado = new Producto(1, "Cera Pomada", cat, 40.0, 15);

        when(categoriaService.buscarPorId(1)).thenReturn(cat);
        when(productoService.guardar(any(Producto.class))).thenReturn(guardado);

        String jsonValido = """
            {
                "nombre": "Cera Pomada",
                "categoria": { "idCategoria": 1 },
                "precio": 40.0,
                "stock": 15
            }
            """;

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idProducto").value(1))
                .andExpect(jsonPath("$.nombre").value("Cera Pomada"))
                .andExpect(jsonPath("$.precio").value(40.0))
                .andExpect(jsonPath("$.stock").value(15));
    }

    @Test
    void testGuardarProductoInvalidoRetornaBadRequest() throws Exception {
        String jsonInvalido = """
            {
                "nombre": "",
                "precio": -5.0,
                "stock": -1
            }
            """;

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.precio").exists())
                .andExpect(jsonPath("$.stock").exists())
                .andExpect(jsonPath("$.categoria").exists());
    }

    @Test
    void testGuardarProductoCategoriaInexistenteRetornaBadRequest() throws Exception {
        when(categoriaService.buscarPorId(999)).thenReturn(null);

        String jsonCategoriaInexistente = """
            {
                "nombre": "Cera Pomada",
                "categoria": { "idCategoria": 999 },
                "precio": 40.0,
                "stock": 15
            }
            """;

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonCategoriaInexistente))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.categoria").exists());
    }

    @Test
    void testActualizarProductoValido() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto existente = new Producto(1, "Cera Pomada", cat, 40.0, 15);
        Producto actualizado = new Producto(1, "Cera Pomada Ultra", cat, 45.0, 20);

        when(productoService.buscarPorId(1)).thenReturn(existente);
        when(categoriaService.buscarPorId(1)).thenReturn(cat);
        when(productoService.guardar(any(Producto.class))).thenReturn(actualizado);

        String jsonValido = """
            {
                "nombre": "Cera Pomada Ultra",
                "categoria": { "idCategoria": 1 },
                "precio": 45.0,
                "stock": 20
            }
            """;

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProducto").value(1))
                .andExpect(jsonPath("$.nombre").value("Cera Pomada Ultra"))
                .andExpect(jsonPath("$.precio").value(45.0));
    }

    @Test
    void testActualizarProductoCategoriaInexistenteRetornaBadRequest() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto existente = new Producto(1, "Cera Pomada", cat, 40.0, 15);

        when(productoService.buscarPorId(1)).thenReturn(existente);
        when(categoriaService.buscarPorId(999)).thenReturn(null);

        String jsonInvalido = """
            {
                "nombre": "Cera Pomada Ultra",
                "categoria": { "idCategoria": 999 },
                "precio": 45.0,
                "stock": 20
            }
            """;

        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.categoria").exists());
    }

    @Test
    void testActualizarProductoNotFound() throws Exception {
        when(productoService.buscarPorId(999)).thenReturn(null);

        String jsonValido = """
            {
                "nombre": "Cera Pomada",
                "categoria": { "idCategoria": 1 },
                "precio": 40.0,
                "stock": 10
            }
            """;

        mockMvc.perform(put("/api/productos/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProductoExistente() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        Producto existente = new Producto(1, "Cera Pomada", cat, 40.0, 15);
        when(productoService.buscarPorId(1)).thenReturn(existente);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProductoNotFound() throws Exception {
        when(productoService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(delete("/api/productos/999"))
                .andExpect(status().isNotFound());
    }
}
