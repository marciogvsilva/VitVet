package br.com.vitvet.controller;

import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.service.SolicitacaoExameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoExameController {

    @Autowired
    private SolicitacaoExameService solicitacaoService;

    @PostMapping
    @PreAuthorize("hasAuthority('VETERINARIO')")
    public ResponseEntity<SolicitacaoExame> criar(@RequestBody SolicitacaoExame novaSolicitacao) {
        SolicitacaoExame solicitacaoCriada = solicitacaoService.criarSolicitacao(novaSolicitacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoCriada);
    }
}