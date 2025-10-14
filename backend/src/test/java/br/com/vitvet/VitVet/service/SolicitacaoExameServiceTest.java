package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.Animal;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.repository.AnimalRepository;
import br.com.vitvet.repository.SolicitacaoExameRepository;
import br.com.vitvet.repository.UsuarioRepository;
import br.com.vitvet.service.SolicitacaoExameService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoExameServiceTest {

    @Mock
    private SolicitacaoExameRepository solicitacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AnimalRepository animalRepository;

    @InjectMocks
    private SolicitacaoExameService solicitacaoService;

    private Usuario veterinario;
    private Animal animal;
    private SolicitacaoExame novaSolicitacao;

    @BeforeEach
    void setUp() {
        veterinario = new Usuario();
        veterinario.setId(1L);

        animal = new Animal();
        animal.setId(1L);

        novaSolicitacao = new SolicitacaoExame();
        novaSolicitacao.setVeterinarioSolicitante(veterinario);
        novaSolicitacao.setAnimal(animal);
        novaSolicitacao.setSuspeitaClinica("Suspeita de teste");
    }

    @Test
    @DisplayName("Deve criar uma nova solicitação de exame com sucesso")
    void deveCriarSolicitacaoComSucesso() {
        // Cenário (Arrange)
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(veterinario));
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(solicitacaoRepository.save(any(SolicitacaoExame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ação (Act)
        SolicitacaoExame solicitacaoCriada = solicitacaoService.criarSolicitacao(novaSolicitacao);

        // Verificação (Assert)
        assertNotNull(solicitacaoCriada);
        assertEquals(StatusSolicitacao.RECEBIDO, solicitacaoCriada.getStatus(), "O status inicial deve ser RECEBIDO.");
        assertNotNull(solicitacaoCriada.getProtocolo(), "O protocolo deve ser gerado.");
        verify(solicitacaoRepository, times(1)).save(novaSolicitacao);
    }

    @Test
    @DisplayName("Deve falhar ao criar solicitação se o veterinário não existir")
    void deveFalharSeVeterinarioNaoExistir() {
        // Cenário (Arrange)
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ação e Verificação (Act & Assert)
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            solicitacaoService.criarSolicitacao(novaSolicitacao);
        });

        assertEquals("Veterinário não encontrado.", exception.getMessage());
        verify(solicitacaoRepository, never()).save(any(SolicitacaoExame.class));
    }

    @Test
    @DisplayName("Deve falhar ao criar solicitação se o animal não existir")
    void deveFalharSeAnimalNaoExistir() {
        // Cenário (Arrange)
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(veterinario));
        when(animalRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Ação e Verificação (Act & Assert)
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            solicitacaoService.criarSolicitacao(novaSolicitacao);
        });

        assertEquals("Animal não encontrado.", exception.getMessage());
        verify(solicitacaoRepository, never()).save(any(SolicitacaoExame.class));
    }

    @Test
    @DisplayName("Deve atualizar o status de uma solicitação com sucesso")
    void deveAtualizarStatusComSucesso() {
        // Cenário (Arrange)
        Long solicitacaoId = 1L;
        SolicitacaoExame solicitacaoExistente = new SolicitacaoExame();
        solicitacaoExistente.setId(solicitacaoId);
        solicitacaoExistente.setStatus(StatusSolicitacao.RECEBIDO);

        when(solicitacaoRepository.findById(solicitacaoId)).thenReturn(Optional.of(solicitacaoExistente));
        when(solicitacaoRepository.save(any(SolicitacaoExame.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ação (Act)
        SolicitacaoExame solicitacaoAtualizada = solicitacaoService.atualizarStatus(solicitacaoId, StatusSolicitacao.EM_ANALISE);

        // Verificação (Assert)
        assertNotNull(solicitacaoAtualizada);
        assertEquals(StatusSolicitacao.EM_ANALISE, solicitacaoAtualizada.getStatus());
        verify(solicitacaoRepository, times(1)).findById(solicitacaoId);
        verify(solicitacaoRepository, times(1)).save(solicitacaoExistente);
    }

    @Test
    @DisplayName("Deve falhar ao atualizar status de solicitação inexistente")
    void deveFalharAoAtualizarStatusDeSolicitacaoInexistente() {
        // Cenário (Arrange)
        Long idInexistente = 99L;
        when(solicitacaoRepository.findById(idInexistente)).thenReturn(Optional.empty());

        // Ação e Verificação (Act & Assert)
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            solicitacaoService.atualizarStatus(idInexistente, StatusSolicitacao.CONCLUIDO);
        });

        assertEquals("Solicitação de exame não encontrada com o ID: " + idInexistente, exception.getMessage());
        verify(solicitacaoRepository, never()).save(any(SolicitacaoExame.class));
    }
}