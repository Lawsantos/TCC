package io.github.cwireset.tcc.request;

import lombok.*;
import io.github.cwireset.tcc.domain.Periodo;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CadastrarReservaRequest {

    private Long idSolicitante;
    private Long idAnuncio;
    private Periodo periodo;
    private Integer quantidadePessoas;

}
