package br.com.vitvet.entities;

import br.com.vitvet.entities.enums.StatusSolicitacao;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "solicitacoes_exames")
@EntityListeners(AuditingEntityListener.class)
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

    @Column
    private String urlResultadoPdf;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataSolicitacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuario veterinarioSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patologista_id")
    private Usuario patologistaResponsavel;

    @ManyToMany
    @JoinTable(
            name = "solicitacao_tipo_exame",
            joinColumns = @JoinColumn(name = "solicitacao_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_exame_id"))
    private List<TipoExame> exames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getSuspeitaClinica() {
        return suspeitaClinica;
    }

    public void setSuspeitaClinica(String suspeitaClinica) {
        this.suspeitaClinica = suspeitaClinica;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public void setStatus(StatusSolicitacao status) {
        this.status = status;
    }

    public String getUrlResultadoPdf() {
        return urlResultadoPdf;
    }

    public void setUrlResultadoPdf(String urlResultadoPdf) {
        this.urlResultadoPdf = urlResultadoPdf;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Usuario getVeterinarioSolicitante() {
        return veterinarioSolicitante;
    }

    public void setVeterinarioSolicitante(Usuario veterinarioSolicitante) {
        this.veterinarioSolicitante = veterinarioSolicitante;
    }

    public Usuario getPatologistaResponsavel() {
        return patologistaResponsavel;
    }

    public void setPatologistaResponsavel(Usuario patologistaResponsavel) {
        this.patologistaResponsavel = patologistaResponsavel;
    }

    public List<TipoExame> getExames() {
        return exames;
    }

    public void setExames(List<TipoExame> exames) {
        this.exames = exames;
    }
}

