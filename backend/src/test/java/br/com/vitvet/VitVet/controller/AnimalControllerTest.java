package br.com.vitvet.VitVet.controller;

import br.com.vitvet.config.security.SecurityConfig;
import br.com.vitvet.config.filter.JwtAuthFilter;
import br.com.vitvet.controller.AnimalController;
import br.com.vitvet.model.Animal;
import br.com.vitvet.service.AnimalService;
import br.com.vitvet.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnimalController.class)
@Import(SecurityConfig.class)
class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AnimalService animalService;

    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Deve criar animal com sucesso (201 Created)")
    @WithMockUser
    void criarAnimal() throws Exception {
        Animal animal = new Animal();
        animal.setNome("Rex");

        when(animalService.criar(any(Animal.class))).thenReturn(animal);

        mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animal)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve listar animais com sucesso (200 OK)")
    @WithMockUser
    void listarAnimais() throws Exception {
        Animal animal = new Animal();
        animal.setNome("Rex");
        when(animalService.listarTodos()).thenReturn(Collections.singletonList(animal));

        mockMvc.perform(get("/api/animais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("Rex")));
    }
}
