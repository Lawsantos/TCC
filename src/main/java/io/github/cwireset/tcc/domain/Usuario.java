package io.github.cwireset.tcc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.Valid;
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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;
    @NotBlank
    @CPF(message = "O CPF deve ser informado no formato 99999999999.")
    @Pattern(regexp="^\\d{11}$")
    private String cpf;
    @NotNull
    private LocalDate dataNascimento;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    @Valid
    private Endereco endereco;

    public Usuario(String nome, String email, String senha, String cpf, LocalDate dataNascimento, String cep, String logradouro, String numero, String complemento, String bairro, String cidade, String estado) {
    }
}
