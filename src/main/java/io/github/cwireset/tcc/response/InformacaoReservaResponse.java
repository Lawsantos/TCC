package io.github.cwireset.tcc.response;

import lombok.*;
import io.github.cwireset.tcc.domain.Pagamento;
import io.github.cwireset.tcc.domain.Periodo;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InformacaoReservaResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Long idReserva;

    @Valid
    private DadosSolicitanteResponse solicitante;

    @NotNull
    private Integer quantidadePessoas;

    @NotNull
    private DadosAnuncioResponse anuncio;

    @Valid
    private Periodo periodo;

    @Valid
    private Pagamento pagamento;


}
