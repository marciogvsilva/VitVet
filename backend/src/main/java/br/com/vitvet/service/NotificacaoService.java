package br.com.vitvet.service;

import br.com.vitvet.model.Notificacao;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.model.enums.Papel;
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

    public void criarNotificacao(Usuario destinatario, String titulo, String mensagem) {
        Notificacao notificacao = new Notificacao();
        notificacao.setDestinatario(destinatario);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setLida(false);

        notificacaoRepository.save(notificacao);
    }

    public void notificarNovaSolicitacao(SolicitacaoExame solicitacao) {
        List<Usuario> patologistas = usuarioRepository.findByPapel(Papel.PATOLOGISTA);

        String titulo = "Nova Solicitação de Exame";
        String mensagem = "O Dr(a). " + solicitacao.getVeterinarioSolicitante().getNome() +
                " solicitou um exame para o animal " + solicitacao.getAnimal().getNome() +
                ". Protocolo: " + solicitacao.getProtocolo();

        for (Usuario patologista : patologistas) {
            criarNotificacao(patologista, titulo, mensagem);
        }
    }

    public void notificarConclusaoExame(SolicitacaoExame solicitacao) {
        Usuario veterinario = solicitacao.getVeterinarioSolicitante();

        String titulo = "Exame Concluído";
        String mensagem = "O exame do animal " + solicitacao.getAnimal().getNome() +
                " (Protocolo: " + solicitacao.getProtocolo() + ") foi concluído e o laudo está disponível.";

        criarNotificacao(veterinario, titulo, mensagem);
    }

    public List<Notificacao> listarMinhasNotificacoes(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado"));

        return notificacaoRepository.findByDestinatarioOrderByDataCriacaoDesc(usuario);
    }

    public void marcarComoLida(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Notificação não encontrada"));

        notificacao.setLida(true);
        notificacaoRepository.save(notificacao);
    }
}