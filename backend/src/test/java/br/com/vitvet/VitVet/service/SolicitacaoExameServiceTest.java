package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.Animal;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.repository.AnimalRepository;
import br.com.vitvet.repository.SolicitacaoExameRepository;
import br.com.vitvet.repository.UsuarioRepository;
import br.com.vitvet.service.NotificacaoService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoExameServiceTest {

    @Mock
    private SolicitacaoExameRepository solicitacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private SolicitacaoExameService solicitacaoService;

    private Usuario veterinario;
    private Animal animal;
    private SolicitacaoExame solicitacao;

    @BeforeEach
    void setUp() {
        veterinario = new Usuario();
        veterinario.setId(1L);
        veterinario.setNome("Dr. Vet");

        animal = new Animal();
        animal.setId(1L);
        animal.setNome("Rex");

        solicitacao = new SolicitacaoExame();
        solicitacao.setId(1L);
        solicitacao.setVeterinarioSolicitante(veterinario);
        solicitacao.setAnimal(animal);
        solicitacao.setStatus(StatusSolicitacao.RECEBIDO);
    }

    @Test
    @DisplayName("Deve criar solicitação com sucesso e notificar patologistas")
    void deveCriarSolicitacaoComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(veterinario));
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(solicitacaoRepository.save(any(SolicitacaoExame.class))).thenAnswer(i -> i.getArgument(0));

        SolicitacaoExame resultado = solicitacaoService.criarSolicitacao(solicitacao);

        assertNotNull(resultado.getProtocolo());
        assertEquals(StatusSolicitacao.RECEBIDO, resultado.getStatus());
        verify(solicitacaoRepository).save(any(SolicitacaoExame.class));
        verify(notificacaoService).notificarNovaSolicitacao(resultado); // Verifica se a notificação foi disparada
    }

    @Test
    @DisplayName("Deve falhar ao criar se veterinário não existir")
    void deveFalharCriacaoSemVeterinario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> solicitacaoService.criarSolicitacao(solicitacao));
        verify(solicitacaoRepository, never()).save(any());
        verify(notificacaoService, never()).notificarNovaSolicitacao(any());
    }

    @Test
    @DisplayName("Deve buscar solicitação por ID com sucesso")
    void deveBuscarPorId() {
        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));

        SolicitacaoExame resultado = solicitacaoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção se solicitação não encontrada por ID")
    void deveFalharBuscarPorIdInexistente() {
        when(solicitacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> solicitacaoService.buscarPorId(99L));
    }

    // --- TESTES DE ATUALIZAÇÃO DE STATUS ---

    @Test
    @DisplayName("Deve atualizar status com sucesso")
    void deveAtualizarStatus() {
        when(solicitacaoRepository.findById(1L)).thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.save(any(SolicitacaoExame.class))).thenAnswer(i -> i.getArgument(0));

        SolicitacaoExame atualizada = solicitacaoService.atualizarStatus(1L, StatusSolicitacao.EM_ANALISE);

        assertEquals(StatusSolicitacao.EM_ANALISE, atualizada.getStatus());
        verify(solicitacaoRepository).save(solicitacao);
    }

    // --- TESTES DE LISTAGEM COM FILTROS ---

    @Test
    @DisplayName("Listar: Deve filtrar por Tutor e Animal")
    void listarFiltroTutorEAnimal() {
        solicitacaoService.listar(null, "Rex", "João");
        verify(solicitacaoRepository).findByAnimalTutorNomeCompletoContainingIgnoreCaseAndAnimalNomeContainingIgnoreCase("João", "Rex");
    }

    @Test
    @DisplayName("Listar: Deve filtrar por Status e Animal")
    void listarFiltroStatusEAnimal() {
        solicitacaoService.listar(StatusSolicitacao.RECEBIDO, "Rex", null);
        verify(solicitacaoRepository).findByStatusAndAnimalNomeContainingIgnoreCase(StatusSolicitacao.RECEBIDO, "Rex");
    }

    @Test
    @DisplayName("Listar: Deve filtrar por Status e Tutor")
    void listarFiltroStatusETutor() {
        solicitacaoService.listar(StatusSolicitacao.RECEBIDO, "João", null);
        verify(solicitacaoRepository).findByStatusAndAnimalNomeContainingIgnoreCase(StatusSolicitacao.RECEBIDO, "João");
    }

    @Test
    @DisplayName("Listar: Deve filtrar apenas por Tutor")
    void listarFiltroApenasTutor() {
        solicitacaoService.listar(null, "João", null);
        verify(solicitacaoRepository).findByAnimalNomeContainingIgnoreCase("João");
    }

    @Test
    @DisplayName("Listar: Deve filtrar apenas por Status")
    void listarFiltroApenasStatus() {
        solicitacaoService.listar(StatusSolicitacao.CONCLUIDO, null, null);
        verify(solicitacaoRepository).findByStatus(StatusSolicitacao.CONCLUIDO);
    }

    @Test
    @DisplayName("Listar: Deve filtrar apenas por Animal")
    void listarFiltroApenasAnimal() {
        solicitacaoService.listar(null, "Rex", null);
        verify(solicitacaoRepository).findByAnimalNomeContainingIgnoreCase("Rex");
    }

    @Test
    @DisplayName("Listar: Deve listar tudo quando sem filtros")
    void listarSemFiltros() {
        solicitacaoService.listar(null, null, null);
        verify(solicitacaoRepository).findAll();
    }
}