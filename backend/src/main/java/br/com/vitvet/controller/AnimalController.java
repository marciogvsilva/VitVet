package br.com.vitvet.controller;

import br.com.vitvet.config.anotation.LogDeAuditoria;
import br.com.vitvet.model.Animal;
import br.com.vitvet.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @PostMapping
    @LogDeAuditoria(acao = "CADASTRO DE ANIMAL")
    public ResponseEntity<Animal> criarAnimal(@RequestBody @Valid Animal animal) {
        Animal animalSalvo = animalService.criar(animal);
        return ResponseEntity.status(HttpStatus.CREATED).body(animalSalvo);
    }

    @GetMapping
    @LogDeAuditoria(acao = "LISTAGEM DE ANIMAIS")
    public ResponseEntity<List<Animal>> listarAnimais() {
        return ResponseEntity.ok(animalService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(animalService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @LogDeAuditoria(acao = "ATUALIZACAO DE ANIMAL")
    public ResponseEntity<Animal> atualizarAnimal(@PathVariable Long id, @RequestBody @Valid Animal animal) {
        return ResponseEntity.ok(animalService.atualizar(id, animal));
    }

    @DeleteMapping("/{id}")
    @LogDeAuditoria(acao = "REMOCAO DE ANIMAL")
    public ResponseEntity<Void> deletarAnimal(@PathVariable Long id) {
        animalService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}