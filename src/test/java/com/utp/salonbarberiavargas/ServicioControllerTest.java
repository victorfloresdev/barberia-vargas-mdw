package com.utp.salonbarberiavargas;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.ServicioController;
import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.ServicioService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ServicioController.class)
class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicioService servicioService;

    @Test
    void testMostrarServiciosWeb() throws Exception {
        Servicio srv = new Servicio(1, "Corte Fade", "Corte clásico", 30, 25.0);
        when(servicioService.listar()).thenReturn(List.of(srv));

        mockMvc.perform(get("/servicios"))
                .andExpect(status().isOk())
                .andExpect(view().name("servicios/servicios"))
                .andExpect(model().attributeExists("servicios"))
                .andExpect(model().attributeExists("nuevoServicio"));
    }

    @Test
    void testAgregarServicioValido() throws Exception {
        when(servicioService.guardar(any(Servicio.class))).thenReturn(new Servicio());

        mockMvc.perform(post("/servicios/guardar")
                .param("nombreServicio", "Barba Spa")
                .param("descripcion", "Perfilado de barba con aceites")
                .param("duracionEstimada", "25")
                .param("precio", "20.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servicios"))
                .andExpect(flash().attributeExists("mensajeExito"));
    }

    @Test
    void testAgregarServicioInvalido() throws Exception {
        mockMvc.perform(post("/servicios/guardar")
                .param("nombreServicio", "")
                .param("descripcion", "")
                .param("duracionEstimada", "-1")
                .param("precio", "-5.00"))
                .andExpect(status().isOk())
                .andExpect(view().name("servicios/servicios"))
                .andExpect(model().attribute("errorModal", "agregar"));
    }

    @Test
    void testEditarServicioValido() throws Exception {
        Servicio srv = new Servicio(1, "Corte Fade", "Corte clásico", 30, 25.0);
        when(servicioService.buscarPorId(1)).thenReturn(srv);
        when(servicioService.guardar(any(Servicio.class))).thenReturn(srv);

        mockMvc.perform(post("/servicios/editar")
                .param("idServicio", "1")
                .param("nombreServicio", "Corte Fade Pro")
                .param("descripcion", "Corte degradado profesional")
                .param("duracionEstimada", "35")
                .param("precio", "30.00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servicios"))
                .andExpect(flash().attributeExists("mensajeExito"));
    }

    @Test
    void testEliminarServicioValido() throws Exception {
        Servicio srv = new Servicio(1, "Corte Fade", "Corte clásico", 30, 25.0);
        when(servicioService.buscarPorId(1)).thenReturn(srv);
        doNothing().when(servicioService).eliminar(1);

        mockMvc.perform(post("/servicios/eliminar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servicios"))
                .andExpect(flash().attributeExists("mensajeExito"));
    }

    @Test
    void testReservarServicioValido() throws Exception {
        Servicio srv = new Servicio(1, "Corte Fade", "Corte clásico", 30, 25.0);
        when(servicioService.buscarPorId(1)).thenReturn(srv);

        mockMvc.perform(post("/servicios/reservar")
                .param("idServicio", "1")
                .param("clienteNombre", "Pedro Castillo")
                .param("clienteTelefono", "987654321")
                .param("fechaCita", "2026-11-01T15:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/servicios"))
                .andExpect(flash().attributeExists("mensajeReserva"));
    }
}
