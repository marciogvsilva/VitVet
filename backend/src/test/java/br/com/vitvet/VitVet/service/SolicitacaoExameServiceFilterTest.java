package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.repository.SolicitacaoExameRepository;
import br.com.vitvet.service.SolicitacaoExameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SolicitacaoExameServiceFilterTest {

    @Mock
    private SolicitacaoExameRepository repository;

    @InjectMocks
    private SolicitacaoExameService service;

    @Test
    @DisplayName("Deve filtrar apenas por status")
    void filtrarPorStatus() {
        service.listar(StatusSolicitacao.CONCLUIDO, null);
        verify(repository).findByStatus(StatusSolicitacao.CONCLUIDO);
    }

    @Test
    @DisplayName("Deve filtrar apenas por nome do animal")
    void filtrarPorAnimal() {
        service.listar(null, "Rex");
        verify(repository).findByAnimalNomeContainingIgnoreCase("Rex");
    }

    @Test
    @DisplayName("Deve filtrar por ambos")
    void filtrarPorAmbos() {
        service.listar(StatusSolicitacao.RECEBIDO, "Rex");
        verify(repository).findByStatusAndAnimalNomeContainingIgnoreCase(StatusSolicitacao.RECEBIDO, "Rex");
    }

    @Test
    @DisplayName("Deve listar todos se nenhum filtro for passado")
    void listarTodosSemFiltro() {
        service.listar(null, null);
        verify(repository).findAll();
    }
}