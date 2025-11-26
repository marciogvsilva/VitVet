package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.Notificacao;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.model.enums.Papel;
import br.com.vitvet.repository.NotificacaoRepository;
import br.com.vitvet.repository.UsuarioRepository;
import br.com.vitvet.service.NotificacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private NotificacaoService service;

    @Test
    @DisplayName("Deve criar notificação corretamente")
    void criarNotificacao() {
        Usuario user = new Usuario();
        service.criarNotificacao(user, "Titulo", "Msg");

        verify(notificacaoRepository).save(any(Notificacao.class));
    }

    @Test
    @DisplayName("Deve listar notificações de um usuário pelo email")
    void listarNotificacoes() {
        String email = "teste@email.com";
        Usuario user = new Usuario();
        user.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(notificacaoRepository.findByDestinatarioOrderByDataCriacaoDesc(user))
                .thenReturn(Collections.emptyList());

        List<Notificacao> resultado = service.listarMinhasNotificacoes(email);
        assertEquals(0, resultado.size());
    }
}
