package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.*;
import lombok.*;

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
