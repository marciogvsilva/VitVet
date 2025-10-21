package br.com.vitvet.model;

import br.com.vitvet.model.enums.StatusSolicitacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "solicitacoes_exames")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class SolicitacaoExame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String protocolo;

    @Column(nullable = false)
    private String suspeitaClinica;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusSolicitacao status;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuario veterinarioSolicitante;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "solicitacao_tipo_exame",
            joinColumns = @JoinColumn(name = "solicitacao_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_exame_id")
    )
    private List<TipoExame> exames;

    @OneToOne(mappedBy = "solicitacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ResultadoExame resultado;

}
