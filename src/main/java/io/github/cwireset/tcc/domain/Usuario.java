package io.github.cwireset.tcc.domain;

import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Table(name = "Usuario", indexes = {
        @Index(name = "idx_usuario_cpf", columnList = "cpf")
})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String nome;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String senha;
    @NotBlank
    @CPF
    @Pattern(regexp="^\\d{11}$")
    //@CPF(@Pattern(regexp = " (\"^[0-9]\\d{11}$\")", value = ))
    private String cpf;
    @NotNull
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    public Usuario(String nome, String email, String senha, String cpf, LocalDate dataNascimento, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) {
    }
}
