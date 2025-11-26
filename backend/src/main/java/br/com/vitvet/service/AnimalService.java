package br.com.vitvet.service;

import br.com.vitvet.model.Animal;
import br.com.vitvet.repository.AnimalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository animalRepository;

    public Animal criar(Animal animal) {
        return animalRepository.save(animal);
    }

    public List<Animal> listarTodos() {
        return animalRepository.findAll();
    }

    public Animal buscarPorId(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com ID: " + id));
    }

    public Animal atualizar(Long id, Animal dadosAtualizados) {
        Animal animalExistente = buscarPorId(id);

        animalExistente.setNome(dadosAtualizados.getNome());
        animalExistente.setEspecie(dadosAtualizados.getEspecie());
        animalExistente.setRaca(dadosAtualizados.getRaca());
        animalExistente.setSexo(dadosAtualizados.getSexo());
        animalExistente.setDataNascimento(dadosAtualizados.getDataNascimento());

        return animalRepository.save(animalExistente);
    }

    public void deletar(Long id) {
        if (!animalRepository.existsById(id)) {
            throw new EntityNotFoundException("Animal não encontrado com ID: " + id);
        }
        animalRepository.deleteById(id);
    }
}