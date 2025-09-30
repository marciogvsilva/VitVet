package br.com.vitvet.service;

import br.com.vitvet.entities.SolicitacaoExame;
import br.com.vitvet.entities.enums.StatusSolicitacao;
import br.com.vitvet.repository.SolicitacaoExameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SolicitacaoExameService {

    @Autowired
    private SolicitacaoExameRepository solicitacaoRepository;

    public SolicitacaoExame criarSolicitacao(SolicitacaoExame novaSolicitacao) {
        novaSolicitacao.setStatus(StatusSolicitacao.RECEBIDO);

        novaSolicitacao.setProtocolo(UUID.randomUUID().toString());

        return solicitacaoRepository.save(novaSolicitacao);
    }
}