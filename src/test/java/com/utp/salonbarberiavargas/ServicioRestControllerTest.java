package com.utp.salonbarberiavargas;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.ServicioRestController;
import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.ServicioService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServicioRestController.class)
class ServicioRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicioService servicioService;

    @Test
    void testListarServiciosRest() throws Exception {
        Servicio srv1 = new Servicio(1, "Corte Clásico", "Corte clásico de caballero", 30, 25.0);
        Servicio srv2 = new Servicio(2, "Perfilado de Barba", "Toalla caliente y navaja", 20, 20.0);

        when(servicioService.listar()).thenReturn(List.of(srv1, srv2));

        mockMvc.perform(get("/api/servicios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idServicio").value(1))
                .andExpect(jsonPath("$[0].nombreServicio").value("Corte Clásico"))
                .andExpect(jsonPath("$[1].idServicio").value(2))
                .andExpect(jsonPath("$[1].nombreServicio").value("Perfilado de Barba"));
    }

    @Test
    void testBuscarPorIdExistente() throws Exception {
        Servicio srv = new Servicio(1, "Corte Clásico", "Corte clásico de caballero", 30, 25.0);
        when(servicioService.buscarPorId(1)).thenReturn(srv);

        mockMvc.perform(get("/api/servicios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.nombreServicio").value("Corte Clásico"));
    }

    @Test
    void testBuscarPorIdNotFound() throws Exception {
        when(servicioService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(get("/api/servicios/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearServicioValido() throws Exception {
        Servicio guardado = new Servicio(1, "Combo Deluxe", "Corte y barba completo", 50, 45.0);
        when(servicioService.guardar(any(Servicio.class))).thenReturn(guardado);

        String jsonValido = """
            {
                "nombreServicio": "Combo Deluxe",
                "descripcion": "Corte y barba completo",
                "duracionEstimada": 50,
                "precio": 45.0
            }
            """;

        mockMvc.perform(post("/api/servicios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idServicio").value(1))
                .andExpect(jsonPath("$.nombreServicio").value("Combo Deluxe"));
    }

    @Test
    void testCrearServicioInvalidoRetornaBadRequest() throws Exception {
        String jsonInvalido = """
            {
                "nombreServicio": "",
                "descripcion": "",
                "duracionEstimada": -5,
                "precio": -10.0
            }
            """;

        mockMvc.perform(post("/api/servicios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreServicio").exists())
                .andExpect(jsonPath("$.descripcion").exists())
                .andExpect(jsonPath("$.duracionEstimada").exists())
                .andExpect(jsonPath("$.precio").exists());
    }

    @Test
    void testActualizarServicioValido() throws Exception {
        Servicio existente = new Servicio(1, "Corte Clásico", "Corte antiguo", 30, 25.0);
        Servicio actualizado = new Servicio(1, "Corte Fade Moderno", "Degradado con máquina", 35, 30.0);

        when(servicioService.buscarPorId(1)).thenReturn(existente);
        when(servicioService.guardar(any(Servicio.class))).thenReturn(actualizado);

        String jsonActualizado = """
            {
                "nombreServicio": "Corte Fade Moderno",
                "descripcion": "Degradado con máquina",
                "duracionEstimada": 35,
                "precio": 30.0
            }
            """;

        mockMvc.perform(put("/api/servicios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonActualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombreServicio").value("Corte Fade Moderno"))
                .andExpect(jsonPath("$.precio").value(30.0));
    }

    @Test
    void testActualizarServicioNotFound() throws Exception {
        when(servicioService.buscarPorId(999)).thenReturn(null);

        String jsonActualizado = """
            {
                "nombreServicio": "Corte Fade Moderno",
                "descripcion": "Degradado con máquina",
                "duracionEstimada": 35,
                "precio": 30.0
            }
            """;

        mockMvc.perform(put("/api/servicios/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonActualizado))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarServicioExistente() throws Exception {
        Servicio existente = new Servicio(1, "Corte Clásico", "Corte clásico", 30, 25.0);
        when(servicioService.buscarPorId(1)).thenReturn(existente);
        doNothing().when(servicioService).eliminar(1);

        mockMvc.perform(delete("/api/servicios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarServicioNotFound() throws Exception {
        when(servicioService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(delete("/api/servicios/999"))
                .andExpect(status().isNotFound());
    }
}
