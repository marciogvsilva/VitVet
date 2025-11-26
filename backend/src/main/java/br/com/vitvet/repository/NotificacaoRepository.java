package br.com.vitvet.repository;

import br.com.vitvet.model.Notificacao;
import br.com.vitvet.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByDestinatarioOrderByDataCriacaoDesc(Usuario destinatario);

    List<Notificacao> findByDestinatarioAndLidaFalseOrderByDataCriacaoDesc(Usuario destinatario);
}