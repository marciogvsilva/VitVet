package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.Tutor;
import br.com.vitvet.repository.TutorRepository;
import br.com.vitvet.service.TutorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TutorServiceTest {

    @Mock
    private TutorRepository repository;

    @InjectMocks
    private TutorService service;

    @Test
    @DisplayName("Deve atualizar tutor com sucesso")
    void atualizarTutor() {
        Tutor antigo = new Tutor();
        antigo.setId(1L);
        antigo.setNomeCompleto("Nome Antigo");

        Tutor novo = new Tutor();
        novo.setNomeCompleto("Nome Novo");
        novo.setEmail("novo@email.com");

        when(repository.findById(1L)).thenReturn(Optional.of(antigo));
        when(repository.save(any(Tutor.class))).thenAnswer(i -> i.getArguments()[0]);

        Tutor atualizado = service.atualizar(1L, novo);

        assertEquals("Nome Novo", atualizado.getNomeCompleto());
        assertEquals("novo@email.com", atualizado.getEmail());
        verify(repository).save(antigo);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar tutor inexistente")
    void atualizarTutorInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            service.atualizar(99L, new Tutor());
        });
    }

    @Test
    @DisplayName("Deve deletar tutor com sucesso")
    void deletarTutor() {
        when(repository.existsById(1L)).thenReturn(true);
        service.deletar(1L);
        verify(repository).deleteById(1L);
    }
}