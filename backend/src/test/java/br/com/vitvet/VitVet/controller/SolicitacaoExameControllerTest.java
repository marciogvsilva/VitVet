package br.com.vitvet.VitVet.controller;

import br.com.vitvet.config.SecurityConfig;
import br.com.vitvet.config.filter.JwtAuthFilter; // Importar o filtro
import br.com.vitvet.controller.SolicitacaoExameController;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.service.JwtService;
import br.com.vitvet.service.SolicitacaoExameService;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SolicitacaoExameController.class)
@Import(SecurityConfig.class)
class SolicitacaoExameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SolicitacaoExameService solicitacaoService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;


    @Test
    @DisplayName("Deve criar uma nova solicitação de exame com sucesso")
    @WithMockUser
    void deveCriarSolicitacaoComSucesso() throws Exception {
        SolicitacaoExame solicitacao = new SolicitacaoExame();
        solicitacao.setProtocolo("ABC-123");

        when(solicitacaoService.criarSolicitacao(any(SolicitacaoExame.class))).thenReturn(solicitacao);

        mockMvc.perform(post("/api/solicitacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new SolicitacaoExame())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.protocolo", is("ABC-123")));
    }

    @Test
    @DisplayName("Deve listar todas as solicitações de exames")
    @WithMockUser
    void deveListarTodasAsSolicitacoes() throws Exception {
        SolicitacaoExame solicitacao = new SolicitacaoExame();
        solicitacao.setProtocolo("ABC-123");
        List<SolicitacaoExame> listaSolicitacoes = Collections.singletonList(solicitacao);

        when(solicitacaoService.listarTodas()).thenReturn(listaSolicitacoes);

        mockMvc.perform(get("/api/solicitacoes"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].protocolo", is("ABC-123")));
    }
}