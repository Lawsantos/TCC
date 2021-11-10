package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.*;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarAnuncioRequest {

    @Enumerated(EnumType.STRING)
    private TipoAnuncio tipoAnuncio;

    private Long idImovel;

    private Long idAnunciante;

    private BigDecimal valorDiaria;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<FormaPagamento> formasAceitas;

    private String descricao;
}
