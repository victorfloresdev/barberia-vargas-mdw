package com.utp.salonbarberiavargas;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.CategoriaRestController;
import com.utp.salonbarberiavargas.model.Categoria;
import com.utp.salonbarberiavargas.service.CategoriaService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoriaRestController.class)
class CategoriaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    @Test
    void testListarCategorias() throws Exception {
        Categoria cat1 = new Categoria(1, "Capilar");
        Categoria cat2 = new Categoria(2, "Tratamiento");

        when(categoriaService.listar()).thenReturn(List.of(cat1, cat2));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCategoria").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Capilar"))
                .andExpect(jsonPath("$[1].idCategoria").value(2))
                .andExpect(jsonPath("$[1].nombre").value("Tratamiento"));
    }

    @Test
    void testBuscarPorIdExistente() throws Exception {
        Categoria cat = new Categoria(1, "Capilar");
        when(categoriaService.buscarPorId(1)).thenReturn(cat);

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nombre").value("Capilar"));
    }

    @Test
    void testBuscarPorIdNotFound() throws Exception {
        when(categoriaService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(get("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGuardarCategoriaValida() throws Exception {
        Categoria guardada = new Categoria(1, "Capilar");
        when(categoriaService.guardar(any(Categoria.class))).thenReturn(guardada);

        String jsonValido = """
            {
                "nombre": "Capilar"
            }
            """;

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nombre").value("Capilar"));
    }

    @Test
    void testGuardarCategoriaInvalidaRetornaBadRequest() throws Exception {
        String jsonInvalido = """
            {
                "nombre": ""
            }
            """;

        mockMvc.perform(post("/api/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists());
    }

    @Test
    void testActualizarCategoriaValida() throws Exception {
        Categoria existente = new Categoria(1, "Capilar");
        Categoria actualizada = new Categoria(1, "Capilar Premium");

        when(categoriaService.buscarPorId(1)).thenReturn(existente);
        when(categoriaService.guardar(any(Categoria.class))).thenReturn(actualizada);

        String jsonValido = """
            {
                "nombre": "Capilar Premium"
            }
            """;

        mockMvc.perform(put("/api/categorias/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCategoria").value(1))
                .andExpect(jsonPath("$.nombre").value("Capilar Premium"));
    }

    @Test
    void testActualizarCategoriaNotFound() throws Exception {
        when(categoriaService.buscarPorId(999)).thenReturn(null);

        String jsonValido = """
            {
                "nombre": "Insumos Varios"
            }
            """;

        mockMvc.perform(put("/api/categorias/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarCategoriaExistente() throws Exception {
        Categoria existente = new Categoria(1, "Capilar");
        when(categoriaService.buscarPorId(1)).thenReturn(existente);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarCategoriaNotFound() throws Exception {
        when(categoriaService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(delete("/api/categorias/999"))
                .andExpect(status().isNotFound());
    }
}
