package com.utp.salonbarberiavargas;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.CitaController;
import com.utp.salonbarberiavargas.model.Cita;
import com.utp.salonbarberiavargas.model.Cliente;
import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.CitaService;
import com.utp.salonbarberiavargas.service.ClienteService;
import com.utp.salonbarberiavargas.service.ServicioService;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CitaService citaService;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ServicioService servicioService;

    private List<Cita> citasPrueba;

    @BeforeEach
    void setUp() {
        citasPrueba = new ArrayList<>();
        Cliente cli1 = new Cliente(1, "Juan Pérez", "987654321", "2026-09-20");
        Cliente cli2 = new Cliente(2, "Carlos Ruiz", "912345678", "2026-09-21");
        Cliente cli3 = new Cliente(3, "Marcos Lima", "998877665", "2026-09-22");

        Servicio srv1 = new Servicio(1, "Corte Fade", "Corte degradado", 30, 25.0);
        Servicio srv2 = new Servicio(2, "Barba Spa", "Perfilado de barba", 20, 20.0);
        Servicio srv3 = new Servicio(3, "Combo VIP", "Corte y barba", 45, 40.0);

        citasPrueba.add(new Cita(101, cli1, srv1, "2026-09-21", "09:00", 25.0, "Confirmada", "Carlos Vargas"));
        citasPrueba.add(new Cita(102, cli2, srv2, "2026-09-22", "11:00", 20.0, "En Curso", "Carlos Vargas"));
        citasPrueba.add(new Cita(103, cli3, srv3, "2026-09-23", "09:00", 40.0, "Pendiente", "Carlos Vargas"));
        citasPrueba.add(new Cita(104, cli1, srv1, "2026-09-24", "15:00", 25.0, "Cancelada", "Carlos Vargas"));

        when(citaService.listar()).thenReturn(citasPrueba);
        when(citaService.guardar(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(clienteService.listar()).thenReturn(List.of(cli1, cli2, cli3));
        when(servicioService.listar()).thenReturn(List.of(srv1, srv2, srv3));
    }

    @Test
    void testMostrarCitas() throws Exception {
        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(view().name("citas/citas"))
                .andExpect(model().attributeExists("listaCitas"))
                .andExpect(model().attributeExists("listaClientes"))
                .andExpect(model().attributeExists("listaServicios"))
                .andExpect(model().attributeExists("citasProgramadas"))
                .andExpect(model().attributeExists("citasAtendidas"))
                .andExpect(model().attributeExists("citasPendientes"))
                .andExpect(content().string(containsString("bg-primary-subtle text-primary border border-primary-subtle rounded-pill")))
                .andExpect(content().string(containsString("bg-success-subtle text-success border border-success-subtle rounded-pill")))
                .andExpect(content().string(containsString("bg-warning-subtle text-warning-emphasis border border-warning-subtle rounded-pill")))
                .andExpect(content().string(containsString("bg-danger-subtle text-danger border border-danger-subtle rounded-pill")))
                .andExpect(content().string(containsString("Juan Pérez")))
                .andExpect(content().string(containsString("Carlos Ruiz")))
                .andExpect(content().string(containsString("Marcos Lima")))
                .andExpect(content().string(containsString("#CT-101")))
                .andExpect(content().string(containsString("action=\"/citas/agregar\"")))
                .andExpect(content().string(containsString("action=\"/citas/editar\"")));
    }

    @Test
    void testAgregarCita() throws Exception {
        mockMvc.perform(post("/citas/agregar")
                .param("idCliente", "1")
                .param("idServicio", "1")
                .param("fecha", "2026-09-25")
                .param("hora", "10:00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/citas"));
    }

    @Test
    void testEditarCita() throws Exception {
        mockMvc.perform(post("/citas/editar")
                .param("idCita", "101")
                .param("idCliente", "1")
                .param("idServicio", "2")
                .param("fecha", "2026-09-21")
                .param("hora", "09:00")
                .param("estadoCita", "En Curso"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/citas"));
    }

    @Test
    void testEliminarCita() throws Exception {
        mockMvc.perform(post("/citas/eliminar/101"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/citas"));
    }
}
