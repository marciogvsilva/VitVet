package br.com.vitvet.controller;

import br.com.vitvet.config.anotation.LogDeAuditoria;
import br.com.vitvet.model.Animal;
import br.com.vitvet.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    @Autowired
    private AnimalRepository animalRepository;

    @PostMapping
    @LogDeAuditoria(acao = "CADASTRO DE ANIMAL")
    public ResponseEntity<Animal> criarAnimal(@RequestBody Animal animal) {
        Animal animalSalvo = animalRepository.save(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalSalvo);
    }

    @GetMapping
    @LogDeAuditoria(acao = "LISTAGEM DE ANIMAIS")
    public ResponseEntity<List<Animal>> listarAnimais() {
        return ResponseEntity.ok(animalRepository.findAll());
    }
}