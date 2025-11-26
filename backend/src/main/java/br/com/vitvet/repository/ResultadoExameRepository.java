package br.com.vitvet.repository;

import br.com.vitvet.model.ResultadoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultadoExameRepository extends JpaRepository<ResultadoExame, Long> {}