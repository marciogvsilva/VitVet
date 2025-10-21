package br.com.vitvet.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "tutores")
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String telefone;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Animal> animais;
}
