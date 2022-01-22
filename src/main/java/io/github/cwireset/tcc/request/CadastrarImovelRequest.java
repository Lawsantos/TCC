package io.github.cwireset.tcc.request;

import lombok.*;
import io.github.cwireset.tcc.domain.CaracteristicaImovel;
import io.github.cwireset.tcc.domain.Endereco;
import io.github.cwireset.tcc.domain.TipoImovel;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarImovelRequest {
    @NotNull
    private Long idProprietario;
    @NotBlank
    private String identificacao;

    @Enumerated(EnumType.STRING)
    @Valid
    @NotNull
    private TipoImovel tipoImovel;

    @NotNull
    @Valid
    private Endereco endereco;

    private List<CaracteristicaImovel> caracteristicas;

}
