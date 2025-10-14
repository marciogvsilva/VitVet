package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.enums.Papel;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.repository.UsuarioRepository;
import br.com.vitvet.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario novoUsuario;

    @BeforeEach
    void setUp() {
        // Cria um objeto de usuário padrão para usar nos testes
        novoUsuario = new Usuario();
        novoUsuario.setNome("Veterinario Teste");
        novoUsuario.setEmail("teste@vitvet.com.br");
        novoUsuario.setSenha("senha123");
        novoUsuario.setCrmv("CRMV-SP 12345");
        novoUsuario.setPapel(Papel.VETERINARIO);
    }

    @Test
    @DisplayName("Deve cadastrar um novo usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {
        // Cenário (Arrange)
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // Simula que o e-mail não existe
        when(passwordEncoder.encode(anyString())).thenReturn("senha_criptografada"); // Simula a criptografia
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(novoUsuario); // Simula o salvamento

        // Ação (Act)
        Usuario usuarioSalvo = usuarioService.cadastrarUsuario(novoUsuario);

        // Verificação (Assert)
        assertNotNull(usuarioSalvo, "O usuário salvo não deve ser nulo.");
        assertEquals("senha_criptografada", usuarioSalvo.getSenha(), "A senha do usuário deve estar criptografada.");
        verify(usuarioRepository, times(1)).findByEmail("teste@vitvet.com.br"); // Verifica se o método findByEmail foi chamado 1 vez
        verify(usuarioRepository, times(1)).save(novoUsuario); // Verifica se o método save foi chamado 1 vez
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao tentar cadastrar e-mail duplicado")
    void deveLancarExcecaoAoCadastrarEmailDuplicado() {
        // Cenário (Arrange)
        when(usuarioRepository.findByEmail("teste@vitvet.com.br")).thenReturn(Optional.of(novoUsuario)); // Simula que o e-mail JÁ EXISTE

        // Ação e Verificação (Act & Assert)
        // Verifica se a exceção esperada é lançada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.cadastrarUsuario(novoUsuario);
        });

        assertEquals("Erro: O e-mail informado já está em uso.", exception.getMessage()); // Verifica a mensagem da exceção
        verify(usuarioRepository, never()).save(any(Usuario.class)); // Garante que o método save NUNCA foi chamado
    }

    @Test
    @DisplayName("Deve garantir que a senha seja criptografada antes de salvar")
    void deveCriptografarSenhaAntesDeSalvar() {
        // Cenário (Arrange)
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Ação (Act)
        usuarioService.cadastrarUsuario(novoUsuario);

        // Verificação (Assert)
        // Verifica se o método encode do passwordEncoder foi chamado exatamente uma vez com a senha original
        verify(passwordEncoder, times(1)).encode("senha123");
    }
}