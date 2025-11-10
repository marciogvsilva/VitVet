package br.com.vitvet.controller;

import br.com.vitvet.config.anotation.LogDeAuditoria;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.service.SolicitacaoExameService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoExameController {

    @Autowired
    private SolicitacaoExameService solicitacaoService;

    @PostMapping
    @PreAuthorize("hasAuthority('VETERINARIO')")
    @LogDeAuditoria(acao = "CRIACAO DE SOLICITACAO")
    public ResponseEntity<SolicitacaoExame> criar(@RequestBody SolicitacaoExame novaSolicitacao) {
        try {
            SolicitacaoExame solicitacaoCriada = solicitacaoService.criarSolicitacao(novaSolicitacao);
            return ResponseEntity.status(HttpStatus.CREATED).body(solicitacaoCriada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @LogDeAuditoria(acao = "LISTAGEM DE SOLICITACOES")
    public ResponseEntity<List<SolicitacaoExame>> listarTodas() {
        List<SolicitacaoExame> solicitacoes = solicitacaoService.listarTodas();
        return ResponseEntity.ok(solicitacoes);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('PATOLOGISTA')")
    @LogDeAuditoria(acao = "ATUALIZACAO DE STATUS")
    public ResponseEntity<SolicitacaoExame> atualizarStatus(
            @PathVariable Long id,
            @RequestBody StatusSolicitacao novoStatus) {
        try {
            SolicitacaoExame solicitacaoAtualizada = solicitacaoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(solicitacaoAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}