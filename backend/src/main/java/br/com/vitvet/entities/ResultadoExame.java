package br.com.vitvet.entities;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_exames")
public class ResultadoExame {

    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    private String urlLaudoPdf;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataLaudo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "solicitacao_id")
    private SolicitacaoExame solicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patologista_id", nullable = false)
    private Usuario patologistaResponsavel;
}
