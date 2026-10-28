package com.utp.salonbarberiavargas;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.CitaRestController;
import com.utp.salonbarberiavargas.model.Cita;
import com.utp.salonbarberiavargas.model.Cliente;
import com.utp.salonbarberiavargas.model.Servicio;
import com.utp.salonbarberiavargas.service.CitaService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CitaRestController.class)
class CitaRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CitaService citaService;

    @Test
    void testListarCitasRest() throws Exception {
        Cliente cli = new Cliente(1, "Juan Pérez", "987654321", "2026-09-20");
        Servicio srv = new Servicio(1, "Corte Fade", "Corte degradado", 30, 25.0);
        Cita cita = new Cita(1, cli, srv, "2026-09-25", "10:00", 25.0, "Confirmada", "Carlos Vargas");

        when(citaService.listar()).thenReturn(List.of(cita));

        mockMvc.perform(get("/api/citas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCita").value(1))
                .andExpect(jsonPath("$[0].estadoCita").value("Confirmada"));
    }

    @Test
    void testCrearCitaRestValida() throws Exception {
        Cliente cli = new Cliente(1, "Juan Pérez", "987654321", "2026-09-20");
        Servicio srv = new Servicio(1, "Corte Fade", "Corte degradado", 30, 25.0);
        Cita guardada = new Cita(1, cli, srv, "2026-09-25", "10:00", 25.0, "Confirmada", "Carlos Vargas");

        when(citaService.guardar(any(Cita.class))).thenReturn(guardada);

        String jsonValido = """
            {
                "cliente": {
                    "id": 1,
                    "nombreCompleto": "Juan Pérez",
                    "telefono": "987654321"
                },
                "servicio": {
                    "id_servicio": 1,
                    "nombre_servicio": "Corte Fade",
                    "descripcion": "Corte degradado",
                    "duracion_estimada": 30,
                    "precio": 25.0
                },
                "fecha": "2026-09-25",
                "hora": "10:00",
                "precio": 25.0,
                "estadoCita": "Confirmada",
                "barbero": "Carlos Vargas"
            }
            """;

        mockMvc.perform(post("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonValido))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCita").value(1));
    }

    @Test
    void testCrearCitaRestInvalidaRetornaBadRequest() throws Exception {
        String jsonInvalido = """
            {
                "fecha": "",
                "hora": "",
                "precio": -10.0,
                "estadoCita": ""
            }
            """;

        mockMvc.perform(post("/api/citas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cliente").exists())
                .andExpect(jsonPath("$.servicio").exists())
                .andExpect(jsonPath("$.fecha").exists())
                .andExpect(jsonPath("$.hora").exists());
    }

    @Test
    void testBuscarPorIdNotFound() throws Exception {
        when(citaService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(get("/api/citas/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarCitaNotFound() throws Exception {
        when(citaService.buscarPorId(999)).thenReturn(null);

        mockMvc.perform(delete("/api/citas/999"))
                .andExpect(status().isNotFound());
    }
}
