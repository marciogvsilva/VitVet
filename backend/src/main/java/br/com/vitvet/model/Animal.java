package br.com.vitvet.model;

import br.com.vitvet.model.enums.Sexo;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "animais")
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String rgAnimal;

    @Column(nullable = false)
    private String especie;

    @Column
    private String raca;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    private LocalDate dataNascimento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", nullable = false)
    @JsonIgnoreProperties("animais")
    private Tutor tutor;

}
