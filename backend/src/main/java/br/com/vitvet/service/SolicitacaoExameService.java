package br.com.vitvet.service;

import br.com.vitvet.model.Animal;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.model.Usuario;
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

    /**
     * Cria uma nova solicitação de exame.
     * Valida se o veterinário e o animal associados existem.
     * Define o status inicial como 'RECEBIDO' e gera um protocolo.
     * @param novaSolicitacao O objeto de solicitação de exame a ser criado.
     * @return A solicitação de exame salva.
     * @throws EntityNotFoundException se o veterinário ou o animal não forem encontrados.
     */
    public SolicitacaoExame criarSolicitacao(SolicitacaoExame novaSolicitacao) {
        Usuario veterinario = usuarioRepository.findById(novaSolicitacao.getVeterinarioSolicitante().getId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado."));

        Animal animal = animalRepository.findById(novaSolicitacao.getAnimal().getId())
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado."));

        novaSolicitacao.setVeterinarioSolicitante(veterinario);
        novaSolicitacao.setAnimal(animal);

        novaSolicitacao.setStatus(StatusSolicitacao.RECEBIDO);
        novaSolicitacao.setProtocolo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return solicitacaoRepository.save(novaSolicitacao);
    }

    /**
     * Atualiza o status de uma solicitação de exame existente.
     * @param id O ID da solicitação a ser atualizada.
     * @param novoStatus O novo status para a solicitação.
     * @return A solicitação de exame atualizada.
     * @throws EntityNotFoundException se a solicitação com o ID fornecido não for encontrada.
     */
    public SolicitacaoExame atualizarStatus(Long id, StatusSolicitacao novoStatus) {
        SolicitacaoExame solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação de exame não encontrada com o ID: " + id));

        solicitacao.setStatus(novoStatus);

        return solicitacaoRepository.save(solicitacao);
    }

    /**
     * Lista todas as solicitações de exame cadastradas.
     * @return Uma lista de todas as solicitações de exame.
     */
    public List<SolicitacaoExame> listarTodas() {
        return solicitacaoRepository.findAll();
    }
}