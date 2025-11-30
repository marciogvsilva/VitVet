package br.com.vitvet.service;

import br.com.vitvet.model.*;
import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.repository.AnimalRepository;
import br.com.vitvet.repository.SolicitacaoExameRepository;
import br.com.vitvet.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SolicitacaoExameService {

    @Autowired
    private SolicitacaoExameRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Cria uma nova solicitação de exame e notifica os patologistas.
     */
    public SolicitacaoExame criarSolicitacao(SolicitacaoExame novaSolicitacao) {
        // Valida e busca as entidades relacionadas
        Usuario veterinario = usuarioRepository.findById(novaSolicitacao.getVeterinarioSolicitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado."));
        Animal animal = animalRepository.findById(novaSolicitacao.getAnimal().getId())
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado."));

        // Preenche os dados da solicitação
        novaSolicitacao.setVeterinarioSolicitante(veterinario);
        novaSolicitacao.setAnimal(animal);
        novaSolicitacao.setStatus(StatusSolicitacao.RECEBIDO);
        // Gera um protocolo único (ex: 8 caracteres maiúsculos)
        novaSolicitacao.setProtocolo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        SolicitacaoExame solicitacaoSalva = solicitacaoRepository.save(novaSolicitacao);

        // Dispara notificação para os patologistas
        notificacaoService.notificarNovaSolicitacao(solicitacaoSalva);

        return solicitacaoSalva;
    }

    /**
     * Busca uma solicitação específica pelo ID.
     */
    public SolicitacaoExame buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada com ID: " + id));
    }

    /**
     * Lista solicitações com base em filtros opcionais.
     * Prioridade: Filtro Combinado (Tutor+Animal) -> Status+Animal -> Status -> Animal -> Tudo
     */
    public List<SolicitacaoExame> listar(StatusSolicitacao status, String nomeAnimal, String nomeTutor) {
        if (nomeTutor != null && nomeAnimal != null) {
            return solicitacaoRepository.findByAnimalTutorNomeCompletoContainingIgnoreCaseAndAnimalNomeContainingIgnoreCase(nomeTutor, nomeAnimal);
        }

        if (status != null && nomeAnimal != null) {
            return solicitacaoRepository.findByStatusAndAnimalNomeContainingIgnoreCase(status, nomeAnimal);
        }

        if (status != null) {
            return solicitacaoRepository.findByStatus(status);
        }

        if (nomeAnimal != null) {
            return solicitacaoRepository.findByAnimalNomeContainingIgnoreCase(nomeAnimal);
        }

        return solicitacaoRepository.findAll();
    }

    /**
     * Atualiza o status de uma solicitação (ex: de RECEBIDO para EM_ANALISE).
     */
    public SolicitacaoExame atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        SolicitacaoExame solicitacao = buscarPorId(id);
        solicitacao.setStatus(novoStatus);

        // Opcional: Adicionar lógica de notificação aqui se o status mudar para algo crítico
        // ex: CANCELADO

        return solicitacaoRepository.save(solicitacao);
    }

    /**
     * Método auxiliar para listar todas (pode ser usado internamente ou mantido para compatibilidade).
     */
    public List<SolicitacaoExame> listarTodas() {
        return solicitacaoRepository.findAll();
    }
}