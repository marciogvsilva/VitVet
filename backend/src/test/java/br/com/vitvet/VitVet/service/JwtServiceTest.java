package br.com.vitvet.VitVet.service;

import br.com.vitvet.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private UserDetails userDetails;
    private final String secretKey = "VGhpcyBJcyBBMjU2Qml0U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg5MA==";

    @BeforeEach
    void setUp() {
        // Injeta a chave secreta no JwtService para os testes, sem precisar do application.properties
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", secretKey);

        // Cria um UserDetails de exemplo
        userDetails = User.builder()
                .username("usuario@teste.com")
                .password("senha")
                .authorities("VETERINARIO")
                .build();
    }

    @Test
    @DisplayName("Deve gerar um token JWT válido")
    void deveGerarTokenValido() {
        // Ação
        String token = jwtService.generateToken(userDetails);

        // Verificação
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve extrair o nome de usuário (email) de um token")
    void deveExtrairUsernameDoToken() {
        // Cenário
        String token = jwtService.generateToken(userDetails);

        // Ação
        String extractedUsername = jwtService.extractUsername(token);

        // Verificação
        assertEquals("usuario@teste.com", extractedUsername);
    }

    @Test
    @DisplayName("Deve validar um token com sucesso")
    void deveValidarTokenComSucesso() {
        // Cenário
        String token = jwtService.generateToken(userDetails);

        // Ação
        boolean isTokenValid = jwtService.isTokenValid(token, userDetails);

        // Verificação
        assertTrue(isTokenValid);
    }

    @Test
    @DisplayName("Deve invalidar um token expirado")
    void deveInvalidarTokenExpirado() {
        // Cenário: Cria um token que expirou 1 segundo atrás
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key signInKey = Keys.hmacShaKeyFor(keyBytes);

        String expiredToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - 10000))
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expiração no passado
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();

        // Ação
        boolean isTokenValid = jwtService.isTokenValid(expiredToken, userDetails);

        // Verificação
        assertFalse(isTokenValid);
    }

    @Test
    @DisplayName("Deve invalidar um token de um usuário diferente")
    void deveInvalidarTokenDeUsuarioDiferente() {
        // Cenário
        String token = jwtService.generateToken(userDetails);
        UserDetails outroUsuario = User.builder()
                .username("outro@usuario.com")
                .password("senha")
                .authorities("PATOLOGISTA")
                .build();

        // Ação
        boolean isTokenValid = jwtService.isTokenValid(token, outroUsuario);

        // Verificação
        assertFalse(isTokenValid);
    }
}
