package br.com.vitvet.VitVet.controller;

import br.com.vitvet.config.security.SecurityConfig; // Importar
import br.com.vitvet.controller.UsuarioController;
import br.com.vitvet.model.enums.Papel;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.service.JwtService;
import br.com.vitvet.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import; // Importar
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@Import(SecurityConfig.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    private Usuario novoUsuario;

    @BeforeEach
    void setUp() {
        novoUsuario = new Usuario();
        novoUsuario.setNome("Veterinario Teste");
        novoUsuario.setEmail("teste@vitvet.com.br");
        novoUsuario.setSenha("senha123");
        novoUsuario.setCrmv("CRMV-SP 12345");
        novoUsuario.setPapel(Papel.VETERINARIO);
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso e retornar status 201")
    void deveCadastrarNovoUsuarioComSucesso() throws Exception {
        when(usuarioService.cadastrarUsuario(any(Usuario.class))).thenReturn(novoUsuario);

        mockMvc.perform(post("/api/usuarios/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is(novoUsuario.getNome())))
                .andExpect(jsonPath("$.email", is(novoUsuario.getEmail())));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request ao tentar cadastrar e-mail duplicado")
    void deveRetornarBadRequestAoCadastrarEmailDuplicado() throws Exception {
        when(usuarioService.cadastrarUsuario(any(Usuario.class)))
                .thenThrow(new RuntimeException("Erro: O e-mail informado já está em uso."));

        mockMvc.perform(post("/api/usuarios/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuario)))
                .andExpect(status().isBadRequest());
    }
}