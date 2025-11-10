package br.com.vitvet.controller;

import br.com.vitvet.config.anotation.LogDeAuditoria;
import br.com.vitvet.model.Tutor;
import br.com.vitvet.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutores")
public class TutorController {

    @Autowired
    private TutorRepository tutorRepository;

    @PostMapping
    @LogDeAuditoria(acao = "CADASTRO DE TUTOR")
    public ResponseEntity<Tutor> criarTutor(@RequestBody Tutor tutor) {
        Tutor tutorSalvo = tutorRepository.save(tutor);
        return ResponseEntity.status(HttpStatus.CREATED).body(tutorSalvo);
    }

    @GetMapping
    @LogDeAuditoria(acao = "LISTAGEM DE TUTORES")
    public ResponseEntity<List<Tutor>> listarTutores() {
        return ResponseEntity.ok(tutorRepository.findAll());
    }
}