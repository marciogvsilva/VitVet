package br.com.vitvet.service;

import br.com.vitvet.model.Notificacao;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.repository.NotificacaoRepository;
import br.com.vitvet.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Cria uma nova notificação para um utilizador.
     */
    public void criarNotificacao(Usuario destinatario, String titulo, String mensagem) {
        Notificacao notificacao = new Notificacao();
        notificacao.setDestinatario(destinatario);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setLida(false);

        notificacaoRepository.save(notificacao);
    }

    /**
     * Lista as notificações do utilizador logado (identificado pelo email).
     */
    public List<Notificacao> listarMinhasNotificacoes(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado"));

        return notificacaoRepository.findByDestinatarioOrderByDataCriacaoDesc(usuario);
    }

    /**
     * Marca uma notificação como lida.
     */
    public void marcarComoLida(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }
}