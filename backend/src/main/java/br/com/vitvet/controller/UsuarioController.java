package br.com.vitvet.controller;

import br.com.vitvet.config.anotation.LogDeAuditoria;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    @LogDeAuditoria(acao = "CADASTRO DE NOVO USUARIO")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario novoUsuario) {
        try {
            Usuario usuarioSalvo = usuarioService.cadastrarUsuario(novoUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}