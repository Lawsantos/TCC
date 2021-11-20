package io.github.cwireset.tcc.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pagamento {

    @NotNull
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @NotBlank
    private FormaPagamento formaEscolhida;

    @Enumerated(EnumType.STRING)
    @NotNull
    private StatusPagamento status;

}
