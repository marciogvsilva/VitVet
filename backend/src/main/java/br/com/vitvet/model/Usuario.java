package br.com.vitvet.model;

import br.com.vitvet.model.enums.Papel;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, unique = true)
    private String crmv;

    @Column
    private String especialidade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Papel papel;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column
    private LocalDateTime dataAtualizacao;
}
