package io.github.cwireset.tcc.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import io.github.cwireset.tcc.domain.Endereco;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtualizarUsuarioRequest {
    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @NotNull
    @Past
    private LocalDate dataNascimento;

    @Valid
    private Endereco endereco;

}
