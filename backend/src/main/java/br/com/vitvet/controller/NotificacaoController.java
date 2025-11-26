package br.com.vitvet.controller;

import br.com.vitvet.config.anotation.LogDeAuditoria;
import br.com.vitvet.model.Notificacao;
import br.com.vitvet.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Lista as notificações do utilizador logado.
     */
    @GetMapping
    @LogDeAuditoria(acao = "CONSULTA DE NOTIFICACOES")
    public ResponseEntity<List<Notificacao>> minhasNotificacoes() {
        // Pega o email do token JWT
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Notificacao> notificacoes = notificacaoService.listarMinhasNotificacoes(emailUsuario);
        return ResponseEntity.ok(notificacoes);
    }

    /**
     * Marca uma notificação específica como lida.
     */
    @PutMapping("/{id}/lida")
    public ResponseEntity<Void> marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.noContent().build();
    }
}