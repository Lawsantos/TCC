package io.github.cwireset.tcc.response;

import lombok.*;
import io.github.cwireset.tcc.domain.FormaPagamento;
import io.github.cwireset.tcc.domain.Imovel;
import io.github.cwireset.tcc.domain.Usuario;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DadosAnuncioResponse {
    private Long id;

    private Imovel imovel;

    private Usuario anunciante;

    private List<FormaPagamento> formasAceitas;

    private String descricao;
}
