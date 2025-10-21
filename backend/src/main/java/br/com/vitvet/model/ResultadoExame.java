package br.com.vitvet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_exames")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
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
    @JsonIgnoreProperties("resultado")
    private Usuario patologistaResponsavel;
}
