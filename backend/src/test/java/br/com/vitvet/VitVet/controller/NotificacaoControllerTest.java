package br.com.vitvet.VitVet.controller;

import br.com.vitvet.config.security.SecurityConfig;
import br.com.vitvet.config.filter.JwtAuthFilter;
import br.com.vitvet.controller.NotificacaoController;
import br.com.vitvet.model.Notificacao;
import br.com.vitvet.service.JwtService;
import br.com.vitvet.service.NotificacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacaoController.class)
@Import(SecurityConfig.class)
class NotificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacaoService notificacaoService;

    @MockBean private JwtService jwtService;
    @MockBean private UserDetailsService userDetailsService;
    @MockBean private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Deve listar notificações do usuário logado")
    @WithMockUser(username = "user@teste.com")
    void listarMinhasNotificacoes() throws Exception {
        Notificacao notif = new Notificacao();
        notif.setTitulo("Teste");

        when(notificacaoService.listarMinhasNotificacoes(anyString()))
                .thenReturn(Collections.singletonList(notif));

        mockMvc.perform(get("/api/notificacoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
