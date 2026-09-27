package com.utp.salonbarberiavargas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.utp.salonbarberiavargas.controller.CitaController;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CitaController.class)
class CitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMostrarCitas() throws Exception {
        mockMvc.perform(get("/citas"))
                .andExpect(status().isOk())
                .andExpect(view().name("citas/citas"))
                .andExpect(model().attributeExists("listaCitas"))
                .andExpect(model().attribute("listaCitas", hasSize(6)))
                // Validar renderizado de badges condicionales
                .andExpect(content().string(containsString("bg-primary-subtle text-primary border border-primary-subtle rounded-pill")))
                .andExpect(content().string(containsString("bg-success-subtle text-success border border-success-subtle rounded-pill")))
                .andExpect(content().string(containsString("bg-warning-subtle text-warning-emphasis border border-warning-subtle rounded-pill")))
                .andExpect(content().string(containsString("bg-danger-subtle text-danger border border-danger-subtle rounded-pill")))
                // Validar presencia de datos de prueba en la tabla
                .andExpect(content().string(containsString("Juan Pérez")))
                .andExpect(content().string(containsString("Carlos Ruiz")))
                .andExpect(content().string(containsString("Marcos Lima")))
                .andExpect(content().string(containsString("#CT-101")))
                // Validar atributos 'name' en inputs del modal para @ModelAttribute
                .andExpect(content().string(containsString("name=\"nombreCliente\"")))
                .andExpect(content().string(containsString("name=\"telefono\"")))
                .andExpect(content().string(containsString("name=\"nombreServicio\"")))
                .andExpect(content().string(containsString("name=\"fecha\"")))
                .andExpect(content().string(containsString("name=\"hora\"")));
    }
}
