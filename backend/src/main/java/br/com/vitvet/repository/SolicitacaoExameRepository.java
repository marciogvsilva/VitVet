package br.com.vitvet.repository;

import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.enums.StatusSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoExameRepository extends JpaRepository<SolicitacaoExame, Long> {

    List<SolicitacaoExame> findByStatus(StatusSolicitacao status);

    List<SolicitacaoExame> findByAnimalNomeContainingIgnoreCase(String nomeAnimal);

    List<SolicitacaoExame> findByStatusAndAnimalNomeContainingIgnoreCase(StatusSolicitacao status, String nomeAnimal);
}
