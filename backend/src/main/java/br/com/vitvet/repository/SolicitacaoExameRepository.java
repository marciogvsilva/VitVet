package br.com.vitvet.repository;

import br.com.vitvet.entities.SolicitacaoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoExameRepository extends JpaRepository<SolicitacaoExame, Long> {

}
