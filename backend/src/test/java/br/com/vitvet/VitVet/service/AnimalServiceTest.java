package br.com.vitvet.VitVet.service;

import br.com.vitvet.model.Animal;
import br.com.vitvet.repository.AnimalRepository;
import br.com.vitvet.service.AnimalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {

    @Mock
    private AnimalRepository repository;

    @InjectMocks
    private AnimalService service;

    @Test
    @DisplayName("Deve criar animal com sucesso")
    void criarAnimal() {
        Animal animal = new Animal();
        animal.setNome("Rex");
        when(repository.save(any(Animal.class))).thenReturn(animal);

        Animal salvo = service.criar(animal);
        assertEquals("Rex", salvo.getNome());
    }

    @Test
    @DisplayName("Deve buscar animal por ID com sucesso")
    void buscarPorId() {
        Animal animal = new Animal();
        animal.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(animal));

        Animal encontrado = service.buscarPorId(1L);
        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar animal inexistente")
    void buscarPorIdInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99L));
    }
}
