package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.enums.Papel;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.repository.UsuarioRepository;
import br.com.vitvet.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("vet@teste.com");
        usuario.setSenha("senhaCriptografada");
        usuario.setPapel(Papel.VETERINARIO);
    }

    @Test
    @DisplayName("Deve carregar o usuário por email com sucesso")
    void deveCarregarUsuarioPorEmailComSucesso() {
        // Cenário (Arrange)
        when(usuarioRepository.findByEmail("vet@teste.com")).thenReturn(Optional.of(usuario));

        // Ação (Act)
        UserDetails userDetails = userDetailsService.loadUserByUsername("vet@teste.com");

        // Verificação (Assert)
        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        assertEquals(usuario.getSenha(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("VETERINARIO")));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o usuário não for encontrado")
    void deveLancarExcecaoSeUsuarioNaoEncontrado() {
        // Cenário (Arrange)
        String emailInexistente = "naoexiste@email.com";
        when(usuarioRepository.findByEmail(emailInexistente)).thenReturn(Optional.empty());

        // Ação e Verificação (Act & Assert)
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(emailInexistente);
        });

        assertEquals("Usuário não encontrado com o e-mail: " + emailInexistente, exception.getMessage());
    }
}
