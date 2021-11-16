package io.github.cwireset.tcc.response;

import io.github.cwireset.tcc.domain.*;
import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoReservaResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    private DadosSolicitanteResponse solicitante;

    private Integer quantidadePessoas;

    private DadosAnuncioResponse anuncio;

    private Periodo periodo;

    private Pagamento pagamento;


}
