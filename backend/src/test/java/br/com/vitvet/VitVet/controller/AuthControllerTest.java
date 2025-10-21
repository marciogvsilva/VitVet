package br.com.vitvet.VitVet.controller;

import br.com.vitvet.config.SecurityConfig;
import br.com.vitvet.controller.AuthController;
import br.com.vitvet.dto.LoginRequest;
import br.com.vitvet.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Deve autenticar usuário com sucesso e retornar token JWT")
    void deveAutenticarUsuarioComSucesso() throws Exception {
        LoginRequest loginRequest = new LoginRequest("usuario@teste.com", "senha123");
        UserDetails userDetails = User.withUsername("usuario@teste.com").password("encodedPassword").authorities("VETERINARIO").build();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("token.jwt.gerado");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token.jwt.gerado"));
    }

    @Test
    @DisplayName("Deve retornar 401 Unauthorized para credenciais inválidas")
    void deveFalharAutenticacaoComCredenciaisInvalidas() throws Exception {
        LoginRequest loginRequest = new LoginRequest("usuario@teste.com", "senha_errada");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}