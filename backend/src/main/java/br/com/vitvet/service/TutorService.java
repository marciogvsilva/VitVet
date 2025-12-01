package br.com.vitvet.service;

import br.com.vitvet.model.Tutor;
import br.com.vitvet.repository.TutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    public Tutor criar(Tutor tutor) {
        return tutorRepository.save(tutor);
    }

    public List<Tutor> listarTodos() {
        return tutorRepository.findAll();
    }

    public Tutor buscarPorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tutor não encontrado com ID: " + id));
    }

    public Tutor atualizar(Long id, Tutor dadosAtualizados) {
        Tutor tutorExistente = buscarPorId(id);

        tutorExistente.setNomeCompleto(dadosAtualizados.getNomeCompleto());
        tutorExistente.setEmail(dadosAtualizados.getEmail());
        tutorExistente.setTelefone(dadosAtualizados.getTelefone());

        return tutorRepository.save(tutorExistente);
    }

    public void deletar(Long id) {
        if (!tutorRepository.existsById(id)) {
            throw new EntityNotFoundException("Tutor não encontrado com ID: " + id);
        }
        tutorRepository.deleteById(id);
    }
}