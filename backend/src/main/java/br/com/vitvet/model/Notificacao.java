package br.com.vitvet.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String mensagem;

    private boolean lida = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

}
