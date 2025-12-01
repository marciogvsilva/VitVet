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

    public SolicitacaoExame criarSolicitacao(SolicitacaoExame novaSolicitacao) {
        Usuario veterinario = usuarioRepository.findById(novaSolicitacao.getVeterinarioSolicitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado."));
        Animal animal = animalRepository.findById(novaSolicitacao.getAnimal().getId())
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado."));

        novaSolicitacao.setVeterinarioSolicitante(veterinario);
        novaSolicitacao.setAnimal(animal);
        novaSolicitacao.setStatus(StatusSolicitacao.RECEBIDO);
        novaSolicitacao.setProtocolo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        SolicitacaoExame solicitacaoSalva = solicitacaoRepository.save(novaSolicitacao);
        notificacaoService.notificarNovaSolicitacao(solicitacaoSalva);

        return solicitacaoSalva;
    }

    public SolicitacaoExame buscarPorId(Long id) {
        return solicitacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada com ID: " + id));
    }

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

    public SolicitacaoExame atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        SolicitacaoExame solicitacao = buscarPorId(id);
        solicitacao.setStatus(novoStatus);

        return solicitacaoRepository.save(solicitacao);
    }

    public List<SolicitacaoExame> listarTodas() {
        return solicitacaoRepository.findAll();
    }
}