package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarAnuncioRequest {

    @Enumerated(EnumType.STRING)
    @NotNull
    @Valid
    private TipoAnuncio tipoAnuncio;

    @NotNull
    private Long idImovel;

    @NotNull
    private Long idAnunciante;

    @NotNull
    private BigDecimal valorDiaria;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @NotNull
    @Valid
    private List<FormaPagamento> formasAceitas;

    @NotBlank
    private String descricao;

    private boolean ativo;
}
